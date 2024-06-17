package org.sanlam.services;

import org.sanlam.helpers.enums.WithdrawalResult;
import org.sanlam.helpers.synchronization.XMutexFactory;
import org.sanlam.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private XMutexFactory intMutexFactory;

    public WithdrawalResult withdraw(Long accountId, BigDecimal amount) {
        synchronized (intMutexFactory.getMutex(accountId)) {
            BigDecimal currentBalance = bankAccountRepository.getCurrentBalance(accountId);

            if (currentBalance == null) {
                return WithdrawalResult.NO_BALANCE;
            } else if (currentBalance.compareTo(amount) < 0) {
                return WithdrawalResult.INSUFFICIENT_FUNDS;
            } else {
                return WithdrawalResult.SUCCESSFUL;
            }
        }
    }

    public BigDecimal getCurrentBalance(Long accountId) {
        return bankAccountRepository.getCurrentBalance(accountId);
    }
}