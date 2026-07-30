package com.ekim.bankingapi.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<Transaction> deposit(@PathVariable Long accountId, @RequestBody Map<String, BigDecimal> body) {
        Transaction transaction = transactionService.deposit(accountId, body.get("amount"));
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<Transaction> withdraw(@PathVariable Long accountId, @RequestBody Map<String, BigDecimal> body) {
        Transaction transaction = transactionService.withdraw(accountId, body.get("amount"));
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId));
    }
}