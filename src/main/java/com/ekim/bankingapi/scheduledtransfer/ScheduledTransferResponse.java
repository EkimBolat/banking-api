package com.ekim.bankingapi.scheduledtransfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ScheduledTransferResponse {

    private Long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private Frequency frequency;
    private LocalDate nextExecutionDate;
    private boolean active;

    public static ScheduledTransferResponse fromEntity(ScheduledTransfer scheduledTransfer) {
        return new ScheduledTransferResponse(
                scheduledTransfer.getId(),
                scheduledTransfer.getFromAccount().getAccountNumber(),
                scheduledTransfer.getToAccount().getAccountNumber(),
                scheduledTransfer.getAmount(),
                scheduledTransfer.getFrequency(),
                scheduledTransfer.getNextExecutionDate(),
                scheduledTransfer.isActive()
        );
    }
}