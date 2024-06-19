package com.example.account.application.service;

import com.example.account.domain.model.AccountEntity;
import com.example.account.domain.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsvServiceTest {

    @InjectMocks
    private CsvService csvService;

    @Mock
    private AccountRepository accountRepository;

    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        String csvContent = "data_vencimento;data_pagamento;valor;descricao;situacao\n" +
                "2024-01-15;2024-01-10;100.50;Conta de Luz;1\n" +
                "2024-02-20;2024-02-18;50.25;Conta de Água;0";
        multipartFile = new MockMultipartFile("file.csv", "file.csv", "text/csv", csvContent.getBytes());
    }

    @Test
    void testImportCSV() {
        csvService.importCSV(multipartFile);

        List<AccountEntity> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(AccountEntity.builder()
                .dueDate(LocalDate.parse("2024-01-15"))
                .paymentDate(LocalDate.parse("2024-01-10"))
                .value(new BigDecimal("100.50"))
                .description("Conta de Luz")
                .status(1)
                .build());
        expectedAccounts.add(AccountEntity.builder()
                .dueDate(LocalDate.parse("2024-02-20"))
                .paymentDate(LocalDate.parse("2024-02-18"))
                .value(new BigDecimal("50.25"))
                .description("Conta de Água")
                .status(0)
                .build());

        verify(accountRepository, times(1)).saveAll(expectedAccounts);
    }

    @Test
    void testImportCSVInvalidFile() {
        MultipartFile invalidFile = new MockMultipartFile("invalid.txt", "invalid.txt", "text/plain", "invalid content".getBytes());

        when(accountRepository.saveAll(anyList())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> csvService.importCSV(invalidFile));

    }
}