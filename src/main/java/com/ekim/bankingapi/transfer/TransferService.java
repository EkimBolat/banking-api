package com.ekim.bankingapi.transfer;

import com.ekim.bankingapi.account.Account;
import com.ekim.bankingapi.account.AccountService;
import com.ekim.bankingapi.exception.InsufficientBalanceException;
import com.ekim.bankingapi.exception.InvalidRequestException;
import com.ekim.bankingapi.transaction.Transaction;
import com.ekim.bankingapi.transaction.TransactionRepository;
import com.ekim.bankingapi.transaction.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        Long fromAccountId = request.getFromAccountId();
        Long toAccountId = request.getToAccountId();
        BigDecimal amount = request.getAmount();

        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidRequestException("Cannot transfer to the same account");
        }

        Account fromAccount = accountService.findAccountEntityById(fromAccountId);
        Account toAccount = accountService.findAccountEntityById(toAccountId);

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        Transfer transfer = new Transfer();
        transfer.setFromAccount(fromAccount);
        transfer.setToAccount(toAccount);
        transfer.setAmount(amount);
        Transfer savedTransfer = transferRepository.save(transfer);

        recordTransaction(fromAccount, TransactionType.WITHDRAWAL, amount, fromAccount.getBalance(), savedTransfer.getId());
        recordTransaction(toAccount, TransactionType.DEPOSIT, amount, toAccount.getBalance(), savedTransfer.getId());

        return TransferResponse.fromEntity(savedTransfer);
    }

    private void recordTransaction(Account account, TransactionType type, BigDecimal amount, BigDecimal balanceAfter, Long transferId) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setTransferId(transferId);
        transactionRepository.save(transaction);
    }
}