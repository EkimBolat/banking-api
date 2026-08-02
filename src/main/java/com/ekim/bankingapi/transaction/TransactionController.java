package com.ekim.bankingapi.transaction;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<TransactionResponse>> getHistory(
            @PathVariable Long accountId,
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId, pageable));
    }
}