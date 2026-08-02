package com.ekim.bankingapi.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountLimitRequest {

    @NotNull(message = "Daily limit is required")
    @Positive(message = "Daily limit must be positive")
    private BigDecimal dailyLimit;

    @NotNull(message = "Monthly limit is required")
    @Positive(message = "Monthly limit must be positive")
    private BigDecimal monthlyLimit;
}