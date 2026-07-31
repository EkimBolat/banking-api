package com.ekim.bankingapi.transaction;

import com.ekim.bankingapi.account.Account;
import com.ekim.bankingapi.account.AccountService;
import com.ekim.bankingapi.exception.InsufficientBalanceException;
import com.ekim.bankingapi.exception.InvalidRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("TR1234567890");
        account.setBalance(BigDecimal.valueOf(500));
    }

    @Test
    void deposit_shouldIncreaseBalance_whenAmountIsValid() {
        when(accountService.findAccountEntityById(1L)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = transactionService.deposit(1L, BigDecimal.valueOf(100));

        assertThat(response.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(response.getBalanceAfter()).isEqualByComparingTo(BigDecimal.valueOf(600));
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(600));
    }

    @Test
    void deposit_shouldThrow_whenAmountIsZeroOrNegative() {
        assertThatThrownBy(() -> transactionService.deposit(1L, BigDecimal.ZERO))
                .isInstanceOf(InvalidRequestException.class);

        assertThatThrownBy(() -> transactionService.deposit(1L, BigDecimal.valueOf(-50)))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    void withdraw_shouldDecreaseBalance_whenSufficientFunds() {
        when(accountService.findAccountEntityById(1L)).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionResponse response = transactionService.withdraw(1L, BigDecimal.valueOf(200));

        assertThat(response.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(response.getBalanceAfter()).isEqualByComparingTo(BigDecimal.valueOf(300));
    }

    @Test
    void withdraw_shouldThrow_whenInsufficientBalance() {
        when(accountService.findAccountEntityById(1L)).thenReturn(account);

        assertThatThrownBy(() -> transactionService.withdraw(1L, BigDecimal.valueOf(999999)))
                .isInstanceOf(InsufficientBalanceException.class);

        verify(transactionRepository, never()).save(any());
    }
}