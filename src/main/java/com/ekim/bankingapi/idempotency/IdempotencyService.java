package com.ekim.bankingapi.idempotency;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final ConcurrentHashMap<String, Object> processedRequests = new ConcurrentHashMap<>();

    public Optional<Object> getCachedResponse(String idempotencyKey) {
        return Optional.ofNullable(processedRequests.get(idempotencyKey));
    }

    public void storeResponse(String idempotencyKey, Object response) {
        processedRequests.put(idempotencyKey, response);
    }
}