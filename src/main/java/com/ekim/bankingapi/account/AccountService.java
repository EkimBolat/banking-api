package com.ekim.bankingapi.account;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private static final SecureRandom RANDOM = new SecureRandom();

    public Account createAccount(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        if (accountRepository.existsByCustomerId(customerId)) {
            throw new RuntimeException("Customer already has an account: " + customerId);
        }

        Account account = new Account();
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateUniqueAccountNumber());

        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private String generateUniqueAccountNumber() {
        String candidate;
        do {
            candidate = generateRandomNumber();
        } while (accountRepository.existsByAccountNumber(candidate));
        return candidate;
    }

    private String generateRandomNumber() {
        StringBuilder sb = new StringBuilder("TR");
        for (int i = 0; i < 10; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}