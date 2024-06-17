package org.sanlam.services;

import org.sanlam.helpers.enums.WithdrawalResult;
import org.sanlam.helpers.synchronization.XMutexFactory;
import org.sanlam.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private XMutexFactory intMutexFactory;

    @Async
    public CompletableFuture<WithdrawalResult> withdraw(Long accountId, BigDecimal amount) {
        synchronized (intMutexFactory.getMutex(accountId)) {
            return bankAccountRepository.getCurrentBalance(accountId)
                .thenApply(currentBalance -> {
                    if (currentBalance == null) {
                        return WithdrawalResult.NO_BALANCE;
                    } else if (currentBalance.compareTo(amount) < 0) {
                        return WithdrawalResult.INSUFFICIENT_FUNDS;
                    } else {
                        return bankAccountRepository.withdraw(accountId, amount)
                            .thenApply(() ->  WithdrawalResult.SUCCESSFUL);
                    }
                });
        }
    }
}