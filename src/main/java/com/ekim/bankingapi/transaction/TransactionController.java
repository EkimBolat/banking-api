package com.ekim.bankingapi.transaction;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{accountId}")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable Long accountId, @Valid @RequestBody AmountRequest request) {
        TransactionResponse transaction = transactionService.deposit(accountId, request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PostMapping("/withdraw/{accountId}")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable Long accountId, @Valid @RequestBody AmountRequest request) {
        TransactionResponse transaction = transactionService.withdraw(accountId, request.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId));
    }
}