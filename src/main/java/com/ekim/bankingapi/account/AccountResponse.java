package com.ekim.bankingapi.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private BigDecimal interestRate;
    private Long customerId;
    private String customerFullName;
    private LocalDateTime createdAt;

    public static AccountResponse fromEntity(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getInterestRate(),
                account.getCustomer().getId(),
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),
                account.getCreatedAt()
        );
    }
}