package com.ekim.bankingapi.customer;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "National ID is required")
    private String nationalId;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Customer must be at least 18 years old")
    private Integer age;

    private String address;
}