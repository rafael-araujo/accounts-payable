package com.example.account.domain.service;

import com.example.account.domain.model.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface AccountService {

    public AccountEntity save(AccountEntity AccountEntity);
    public AccountEntity update(Integer id, AccountEntity AccountEntity);
    public void updateStatus(Integer id, Integer status);
    public Page<AccountEntity> findAll(LocalDate dueDate, String description, Pageable pageable);
    public Optional<AccountEntity> findById(Integer id);
    public BigDecimal getTotalPayByPeriod(LocalDate startDate, LocalDate endDate);
}
