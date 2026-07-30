package com.ekim.bankingapi.transfer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<Transfer> transfer(@RequestBody Map<String, Object> body) {
        Long fromAccountId = Long.valueOf(body.get("fromAccountId").toString());
        Long toAccountId = Long.valueOf(body.get("toAccountId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());

        Transfer result = transferService.transfer(fromAccountId, toAccountId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}