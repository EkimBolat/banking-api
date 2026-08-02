package com.ekim.bankingapi.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);

    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);
}