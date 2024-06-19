package com.example.account.application.service;


import com.example.account.domain.model.AccountEntity;
import com.example.account.domain.repository.AccountRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    @Autowired
    private AccountRepository accountRepository;

    public void importCSV(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim()
                     .withDelimiter(';'))) {

            List<AccountEntity> accounts = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                AccountEntity account = AccountEntity.builder()
                    .dueDate(LocalDate.parse(csvRecord.get("data_vencimento")))
                    .paymentDate(LocalDate.parse(csvRecord.get("data_pagamento")))
                    .value(new BigDecimal(csvRecord.get("valor")))
                    .description(csvRecord.get("descricao"))
                    .status(Integer.valueOf(csvRecord.get("situacao")))
                    .build();
                accounts.add(account);
            }

            accountRepository.saveAll(accounts);
        } catch (Exception e) {
            throw new RuntimeException("Error to Import CSV: " + e.getMessage());
        }
    }
}
