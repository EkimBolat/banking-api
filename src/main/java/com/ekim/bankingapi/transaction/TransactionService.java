package com.ekim.bankingapi.transaction;

import com.ekim.bankingapi.account.Account;
import com.ekim.bankingapi.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return saveTransaction(account, TransactionType.DEPOSIT, amount, newBalance);
    }

    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount) {
        validateAmount(amount);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance. Current balance: " + account.getBalance());
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return saveTransaction(account, TransactionType.WITHDRAWAL, amount, newBalance);
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);
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
            throw new RuntimeException("Amount must be greater than zero");
        }
    }
}