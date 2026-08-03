package com.ekim.bankingapi.scheduledtransfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ScheduledTransferRequest {

    @NotNull(message = "Source account id is required")
    private Long fromAccountId;

    @NotNull(message = "Destination account id is required")
    private Long toAccountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Frequency is required")
    private Frequency frequency;
}