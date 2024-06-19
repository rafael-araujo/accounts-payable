package com.example.account.domain.service;

import com.example.account.domain.model.AccountEntity;
import com.example.account.domain.repository.AccountRepository;
import com.example.account.domain.service.impl.AccountServiceImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        AccountEntity accountEntity = new AccountEntity();
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        AccountEntity savedAccountEntity = accountService.save(accountEntity);

        assertEquals(accountEntity, savedAccountEntity);
    }

    @Test
    void testUpdate() {
        Integer id = 1;
        AccountEntity existingAccountEntity = new AccountEntity();
        existingAccountEntity.setAccountId(id);
        AccountEntity updatedAccountEntity = new AccountEntity();
        updatedAccountEntity.setAccountId(id);
        when(accountRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(existingAccountEntity));
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(updatedAccountEntity);

        AccountEntity result = accountService.update(id, updatedAccountEntity);

        assertEquals(updatedAccountEntity, result);
    }

    @Test
    void testUpdateNotFound() {
        Integer id = 1;
        when(accountRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());

        AccountEntity result = accountService.update(id, new AccountEntity());

        assertNull(result);
    }

    @Test
    void testUpdateStatus() {
        Integer id = 1;
        Integer status = 2;
        AccountEntity existingAccountEntity = new AccountEntity();
        existingAccountEntity.setAccountId(id);
        when(accountRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(existingAccountEntity));

        accountService.updateStatus(id, status);

        assertEquals(status, existingAccountEntity.getStatus());
    }

//    @Test
//    void testFindAll() {
//        LocalDate dueDate = LocalDate.of(2023, 4, 25);
//        String description = "Test";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        List<AccountEntity> accountEntities = new ArrayList<>();
//        Page<AccountEntity> expectedPage = new PageImpl<>(accountEntities, pageable, accountEntities.size());
//        when(accountRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);
//
//        Page<AccountEntity> result = accountService.findAll(dueDate, description, pageable);
//
//        assertEquals(expectedPage, result);
//    }

    @Test
    void testFindById() {
        Integer id = 1;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(id);
        when(accountRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(accountEntity));

        Optional<AccountEntity> result = accountService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(accountEntity, result.get());
    }

    @Test
    void testGetTotalPayByPeriod() {
        LocalDate startDate = LocalDate.of(2023, 4, 1);
        LocalDate endDate = LocalDate.of(2023, 4, 30);
        List<AccountEntity> accountEntities = new ArrayList<>();
        accountEntities.add(AccountEntity.builder().value(BigDecimal.valueOf(100)).build());
        accountEntities.add(AccountEntity.builder().value(BigDecimal.valueOf(100)).build());
        when(accountRepository.findByStatusAndPaymentDateBetween(startDate, endDate)).thenReturn(accountEntities);

        BigDecimal result = accountService.getTotalPayByPeriod(startDate, endDate);

        assertEquals(BigDecimal.valueOf(200), result);
    }
}
