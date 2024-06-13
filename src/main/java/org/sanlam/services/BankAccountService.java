package org.sanlam.services;

import org.sanlam.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public String withdraw(Long accountId, BigDecimal amount) {
        BigDecimal currentBalance = bankAccountRepository.getCurrentBalance(accountId);

        if (currentBalance == null) {
            return "CURRENT_BALANCE_NULL";
        } else if (currentBalance.compareTo(amount) < 0) {
            return "INSUFFICIENT_FUNDS";
        } else {
            return "WITHDRAWAL_SUCCESSFUL";
        }
    }

    public BigDecimal getCurrentBalance(Long accountId) {
        return bankAccountRepository.getCurrentBalance(accountId);
    }
}