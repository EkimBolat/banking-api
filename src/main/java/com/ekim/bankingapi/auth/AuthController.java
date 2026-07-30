package com.ekim.bankingapi.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> body) {
        Long customerId = Long.valueOf(body.get("customerId").toString());
        String email = body.get("email").toString();
        String password = body.get("password").toString();

        User user = authService.register(customerId, email, password);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "customerId", user.getCustomer().getId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> body) {
        String email = body.get("email").toString();
        String password = body.get("password").toString();

        User user = authService.login(email, password);

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "userId", user.getId(),
                "customerId", user.getCustomer().getId()
        ));
    }
}