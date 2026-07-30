package com.ekim.bankingapi.transaction;

import com.ekim.bankingapi.account.Account;
import com.ekim.bankingapi.account.AccountService;
import com.ekim.bankingapi.exception.InsufficientBalanceException;
import com.ekim.bankingapi.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Transactional
    public TransactionResponse deposit(Long accountId, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountService.findAccountEntityById(accountId);

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);

        Transaction transaction = saveTransaction(account, TransactionType.DEPOSIT, amount, newBalance);
        return TransactionResponse.fromEntity(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(Long accountId, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountService.findAccountEntityById(accountId);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance. Current balance: " + account.getBalance());
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        Transaction transaction = saveTransaction(account, TransactionType.WITHDRAWAL, amount, newBalance);
        return TransactionResponse.fromEntity(transaction);
    }

    public List<TransactionResponse> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId).stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    private Transaction saveTransaction(Account account, TransactionType type, BigDecimal amount, BigDecimal balanceAfter) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        return transactionRepository.save(transaction);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }
    }
}