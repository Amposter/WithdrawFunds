package org.sanlam.controllers;

import org.sanlam.enums.WithdrawalResult;
import org.sanlam.services.SnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;

import org.sanlam.services.BankAccountService;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private SnsService snsService;

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
        @RequestParam("accountId") Long accountId,
        @RequestParam("amount") BigDecimal amount
    ) {
        WithdrawalResult result = bankAccountService.withdraw(accountId, amount);

        switch (result) {
            case WithdrawalResult.NO_BALANCE -> {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("No account for specified ID");
            }
            case WithdrawalResult.INSUFFICIENT_FUNDS -> {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Insufficient funds for withdrawal");
            }
            case WithdrawalResult.SUCCESSFUL -> {
                // After a successful withdrawal, publish a withdrawal event to SNS
                snsService.publishWithdrawalEvent(amount, accountId, "SUCCESSFUL");
                return ResponseEntity.ok("Withdrawal successful");
            }
            default -> {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
            }
        }
    }
}