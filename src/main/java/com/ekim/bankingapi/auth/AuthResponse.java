package com.ekim.bankingapi.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private Long customerId;
    private String email;
    private String message;
    private String token;
    private Role role;
    private String refreshToken;
}