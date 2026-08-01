package com.ekim.bankingapi.account;

import com.ekim.bankingapi.branch.Branch;
import com.ekim.bankingapi.branch.BranchService;
import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.InvalidRequestException;
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
    private final BranchService branchService;
    private static final SecureRandom RANDOM = new SecureRandom();

    public AccountResponse createAccount(Long customerId, AccountRequest request) {
        Customer customer = customerService.findCustomerEntityById(customerId);

        if (accountRepository.existsByCustomerId(customerId)) {
            throw new DuplicateResourceException("Customer already has an account: " + customerId);
        }

        validateAccountTypeRules(request);

        Account account = new Account();
        account.setCustomer(customer);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateUniqueAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setInterestRate(request.getAccountType() == AccountType.SAVINGS ? request.getInterestRate() : null);

        if (request.getBranchId() != null) {
            Branch branch = branchService.findBranchEntityById(request.getBranchId());
            account.setBranch(branch);
        }

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

    private void validateAccountTypeRules(AccountRequest request) {
        if (request.getAccountType() == AccountType.SAVINGS) {
            if (request.getInterestRate() == null || request.getInterestRate().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidRequestException("Savings accounts require a positive interest rate");
            }
        } else if (request.getAccountType() == AccountType.CHECKING) {
            if (request.getInterestRate() != null) {
                throw new InvalidRequestException("Checking accounts cannot have an interest rate");
            }
        }
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

    public Account findAccountEntityById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }
}