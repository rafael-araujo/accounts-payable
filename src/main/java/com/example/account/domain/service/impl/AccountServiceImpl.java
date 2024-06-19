package com.example.account.domain.service.impl;

import com.example.account.domain.model.AccountEntity;
import com.example.account.domain.repository.AccountRepository;
import com.example.account.domain.service.AccountService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl  implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity save(AccountEntity AccountEntity) {
        return accountRepository.save(AccountEntity);
    }

    public AccountEntity update(Integer id, AccountEntity AccountEntity) {
        Optional<AccountEntity> existingAccountEntity = accountRepository.findById(Long.valueOf(id));
        if (existingAccountEntity.isPresent()) {
            AccountEntity updatedAccountEntity = existingAccountEntity.get();
            updatedAccountEntity.setDueDate(AccountEntity.getDueDate());
            updatedAccountEntity.setPaymentDate(AccountEntity.getPaymentDate());
            updatedAccountEntity.setValue(AccountEntity.getValue());
            updatedAccountEntity.setDescription(AccountEntity.getDescription());
            updatedAccountEntity.setStatus(AccountEntity.getStatus());
            return accountRepository.save(updatedAccountEntity);
        }
        return null;
    }

    public void updateStatus(Integer id, Integer status) {
        Optional<AccountEntity> existingAccountEntity = accountRepository.findById(Long.valueOf(id));
        if (existingAccountEntity.isPresent()) {
            AccountEntity AccountEntity = existingAccountEntity.get();
            AccountEntity.setStatus(status);
            accountRepository.save(AccountEntity);
        }
    }

    public Page<AccountEntity> findAll(LocalDate dueDate, String description, Pageable pageable) {

        return accountRepository.findAll(
                (root, query, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (dueDate != null) {
                        predicates.add(cb.equal(root.get("dueDate"), dueDate));
                    }
                    if (description != null) {
                        predicates.add(cb.like(root.get("description"), "%" + description + "%"));
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                },
                pageable);
    }

    public Optional<AccountEntity> findById(Integer id) {
        return accountRepository.findById(Long.valueOf(id));
    }

    public BigDecimal getTotalPayByPeriod(LocalDate startDate, LocalDate endDate) {
        List<AccountEntity> accountEntities = accountRepository.findByStatusAndPaymentDateBetween(startDate, endDate);
        return accountEntities.stream()
                .map(AccountEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
