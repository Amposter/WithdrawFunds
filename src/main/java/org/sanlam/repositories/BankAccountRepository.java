package org.sanlam.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Repository
public class BankAccountRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Async("dbThreadPoolTaskExecutor")
    public CompletableFuture<BigDecimal> getCurrentBalance(Long accountId) {
        CompletableFuture<BigDecimal> currentBalanceFuture = CompletableFuture.supplyAsync( () -> {
            String sql = "SELECT balance FROM accounts WHERE id = ?";
            return jdbcTemplate.queryForObject(
                sql, new Object[]{accountId}, BigDecimal.class
            );
        });
        return currentBalanceFuture;
    }

    @Async("dbThreadPoolTaskExecutor")
    public CompletableFuture<Integer> withdraw(Long accountId, BigDecimal amount) {
        CompletableFuture<Integer> rowsUpdatedFuture = CompletableFuture.supplyAsync( () -> {
            String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
            return jdbcTemplate.update(sql, amount, accountId);
        });

        return rowsUpdatedFuture;
    }
}
