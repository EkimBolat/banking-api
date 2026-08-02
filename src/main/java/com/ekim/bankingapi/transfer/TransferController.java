package com.ekim.bankingapi.transfer;

import com.ekim.bankingapi.idempotency.IdempotencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final IdempotencyService idempotencyService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        if (idempotencyKey != null) {
            var cached = idempotencyService.getCachedResponse(idempotencyKey);
            if (cached.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body((TransferResponse) cached.get());
            }
        }

        TransferResponse result = transferService.transfer(request);

        if (idempotencyKey != null) {
            idempotencyService.storeResponse(idempotencyKey, result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}