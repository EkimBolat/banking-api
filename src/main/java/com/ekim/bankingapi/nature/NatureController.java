package com.ekim.bankingapi.nature;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/nature")
@RequiredArgsConstructor
public class NatureController {

    private final NatureService natureService;

    @GetMapping("/certificates/customer/{customerId}")
    public ResponseEntity<List<TreeCertificateResponse>> getCertificates(@PathVariable Long customerId) {
        return ResponseEntity.ok(natureService.getCertificatesForCustomer(customerId));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(Map.of("totalTreesPlanted", natureService.getTotalTreesPlanted()));
    }
}