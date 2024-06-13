package org.sanlam.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class BankAccountRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public BigDecimal getCurrentBalance(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal currentBalance = jdbcTemplate.queryForObject(
            sql, new Object[]{accountId}, BigDecimal.class
        );
        return currentBalance;
    }

    public Integer updateBalance(Long accountId, BigDecimal amount) {
        String sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        Integer rowsUpdated = jdbcTemplate.update(sql, amount, accountId);
        return rowsUpdated;
    }
}
