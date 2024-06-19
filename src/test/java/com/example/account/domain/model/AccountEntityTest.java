package com.example.account.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountEntityTest {

    @Test
    void testAccountEntityCreation() {
        // Given
        Integer accountId = 1;
        LocalDate dueDate = LocalDate.of(2024, 1, 1);
        LocalDate paymentDate = LocalDate.of(2024, 2, 1);
        BigDecimal value = BigDecimal.valueOf(100.00);
        String description = "Test Description";
        Integer status = 1;

        // When
        AccountEntity accountEntity = AccountEntity.builder()
                .accountId(accountId)
                .dueDate(dueDate)
                .paymentDate(paymentDate)
                .value(value)
                .description(description)
                .status(status)
                .build();

        // Then
        assertEquals(accountId, accountEntity.getAccountId());
        assertEquals(dueDate, accountEntity.getDueDate());
        assertEquals(paymentDate, accountEntity.getPaymentDate());
        assertEquals(value, accountEntity.getValue());
        assertEquals(description, accountEntity.getDescription());
        assertEquals(status, accountEntity.getStatus());
    }

    @Test
    void testAccountEntityGettersAndSetters() {
        // Given
        AccountEntity accountEntity = new AccountEntity();

        // When & Then
        accountEntity.setAccountId(1);
        assertEquals(1, accountEntity.getAccountId());

        accountEntity.setDueDate(LocalDate.of(2024, 1, 1));
        assertEquals(LocalDate.of(2024, 1, 1), accountEntity.getDueDate());

        accountEntity.setPaymentDate(LocalDate.of(2024, 2, 1));
        assertEquals(LocalDate.of(2024, 2, 1), accountEntity.getPaymentDate());

        accountEntity.setValue(BigDecimal.valueOf(100.00));
        assertEquals(BigDecimal.valueOf(100.00), accountEntity.getValue());

        accountEntity.setDescription("Test Description");
        assertEquals("Test Description", accountEntity.getDescription());

        accountEntity.setStatus(1);
        assertEquals(1, accountEntity.getStatus());
    }
}