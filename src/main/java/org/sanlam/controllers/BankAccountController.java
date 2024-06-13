package org.sanlam.controllers;

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
        String result = bankAccountService.withdraw(accountId, amount);

        switch (result) {
            case "CURRENT_BALANCE_NULL" -> {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Current balance is null");
            }
            case "INSUFFICIENT_FUNDS" -> {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Insufficient funds for withdrawal");
            }
            case "WITHDRAWAL_SUCCESSFUL" -> {
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