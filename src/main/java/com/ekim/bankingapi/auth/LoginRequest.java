package com.ekim.bankingapi.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "National ID is required")
    private String nationalId;

    @NotBlank(message = "Password is required")
    private String password;
}