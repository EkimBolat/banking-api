package com.ekim.bankingapi.account;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private static final SecureRandom RANDOM = new SecureRandom();

    public AccountResponse createAccount(Long customerId) {
        Customer customer = customerService.findCustomerEntityById(customerId);

        if (accountRepository.existsByCustomerId(customerId)) {
            throw new DuplicateResourceException("Customer already has an account: " + customerId);
        }

        Account account = new Account();
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateUniqueAccountNumber());

        Account saved = accountRepository.save(account);
        return AccountResponse.fromEntity(saved);
    }

    public AccountResponse getAccountById(Long id) {
        Account account = findAccountEntityById(id);
        return AccountResponse.fromEntity(account);
    }

    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));
        return AccountResponse.fromEntity(account);
    }

    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountResponse::fromEntity)
                .toList();
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

    // Transaction ve Transfer service'leri Account entity'sine ihtiyaç duyduğu için bunu koruyoruz
    public Account findAccountEntityById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }
}