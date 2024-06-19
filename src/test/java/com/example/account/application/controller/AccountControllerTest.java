package com.example.account.application.controller;

import com.example.account.application.service.CsvService;
import com.example.account.domain.model.AccountEntity;
import com.example.account.domain.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private CsvService csvService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        AccountEntity account = new AccountEntity();
        account.setDescription("Test");

        when(accountService.save(any(AccountEntity.class))).thenReturn(account);

        ResponseEntity<AccountEntity> response = accountController.createAccount(account);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetAllAccounts() {
        Page<AccountEntity> accounts = new PageImpl<>(Collections.singletonList(new AccountEntity()));
        when(accountService.findAll(any(), any(), any(Pageable.class))).thenReturn(accounts);

        ResponseEntity<Page<AccountEntity>> response = accountController.getAllAccounts(null, null, Pageable.ofSize(10));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }

    @Test
    void testUpdateAccount() {
        Integer id = 1;
        AccountEntity account = new AccountEntity();
        account.setAccountId(id);
        account.setDescription("Updated");

        when(accountService.update(anyInt(), any(AccountEntity.class))).thenReturn(account);

        ResponseEntity<AccountEntity> response = accountController.updateAccount(id, account);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetContaById() {
        Integer id = 1;
        AccountEntity account = new AccountEntity();
        account.setAccountId(id);

        when(accountService.findById(anyInt())).thenReturn(Optional.of(account));

        ResponseEntity<AccountEntity> response = accountController.getContaById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testGetContaByIdNotFound() {
        when(accountService.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<AccountEntity> response = accountController.getContaById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateStatus() {
        Integer id = 1;
        int status = 1;

        ResponseEntity<Void> response = accountController.updateStatus(id, status);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetTotalPayByPeriod() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        BigDecimal totalPay = BigDecimal.TEN;

        when(accountService.getTotalPayByPeriod(any(), any())).thenReturn(totalPay);

        ResponseEntity<BigDecimal> response = accountController.getTotalPayByPeriod(startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalPay, response.getBody());
    }

    @Test
    void testImportCSV() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "test".getBytes());

        Mockito.doNothing().when(csvService).importCSV(any());

        ResponseEntity<Void> response = accountController.importCSV(file);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
