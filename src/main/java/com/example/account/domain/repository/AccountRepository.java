package com.example.account.domain.repository;


import com.example.account.domain.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>, JpaSpecificationExecutor<AccountEntity> {

    @Query("SELECT a FROM AccountEntity a WHERE a.status = 2 AND a.paymentDate BETWEEN :startDate AND :endDate")
    List<AccountEntity> findByStatusAndPaymentDateBetween(LocalDate startDate, LocalDate endDate);
}
