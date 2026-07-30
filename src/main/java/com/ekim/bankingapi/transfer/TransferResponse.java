package com.ekim.bankingapi.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransferResponse {

    private Long id;
    private String fromAccountNumber;
    private String toAccountNumber;
    private BigDecimal amount;
    private LocalDateTime timestamp;

    public static TransferResponse fromEntity(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getFromAccount().getAccountNumber(),
                transfer.getToAccount().getAccountNumber(),
                transfer.getAmount(),
                transfer.getTimestamp()
        );
    }
}