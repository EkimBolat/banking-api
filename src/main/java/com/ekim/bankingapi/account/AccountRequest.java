package com.ekim.bankingapi.account;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    private BigDecimal interestRate;

    private Long branchId;
}