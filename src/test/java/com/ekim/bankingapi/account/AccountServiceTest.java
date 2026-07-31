package com.ekim.bankingapi.account;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Ahmet");
        customer.setLastName("Yılmaz");
    }

    @Test
    void createAccount_shouldSucceed_whenCustomerHasNoAccount() {
        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);
        when(accountRepository.existsByCustomerId(1L)).thenReturn(false);
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account acc = invocation.getArgument(0);
            acc.setId(1L);
            return acc;
        });

        AccountResponse response = accountService.createAccount(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.getCustomerFullName()).isEqualTo("Ahmet Yılmaz");
        assertThat(response.getAccountNumber()).startsWith("TR");
    }

    @Test
    void createAccount_shouldThrow_whenCustomerAlreadyHasAccount() {
        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);
        when(accountRepository.existsByCustomerId(1L)).thenReturn(true);

        assertThatThrownBy(() -> accountService.createAccount(1L))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already has an account");

        verify(accountRepository, never()).save(any());
    }

    @Test
    void getAccountById_shouldThrow_whenNotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}