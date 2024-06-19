package com.example.account.application.controller;

import com.example.account.application.service.CsvService;
import com.example.account.domain.model.AccountEntity;
import com.example.account.domain.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CsvService csvService;

    @PostMapping
    public ResponseEntity<AccountEntity> createAccount(@Valid @RequestBody AccountEntity account) {
        AccountEntity savedAccount= accountService.save(account);
        return ResponseEntity.ok(savedAccount);
    }

    @GetMapping
    public ResponseEntity<Page<AccountEntity>> getAllAccounts(
            @RequestParam(value = "dueDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam(value = "description", required = false) String description,
            Pageable pageable) {

        Page<AccountEntity> accounts = accountService.findAll(dueDate, description, pageable);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountEntity> updateAccount(@PathVariable Integer id, @Valid @RequestBody AccountEntity account) {
        AccountEntity updatedAccount = accountService.update(id, account);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountEntity> getContaById(@PathVariable Integer id) {
        Optional<AccountEntity> account = accountService.findById(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Void> updateStatus(@PathVariable Integer id, @PathVariable Integer status) {
        accountService.updateStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total-pay")
    public ResponseEntity<BigDecimal> getTotalPayByPeriod(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        BigDecimal totalPay = accountService.getTotalPayByPeriod(startDate, endDate);
        return ResponseEntity.ok(totalPay);
    }

    @PostMapping("/import")
    public ResponseEntity<Void> importCSV(@RequestParam("file") MultipartFile file) {
        csvService.importCSV(file);
        return ResponseEntity.noContent().build();
    }
}
