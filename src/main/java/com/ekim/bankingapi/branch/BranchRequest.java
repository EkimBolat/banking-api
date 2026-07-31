package com.ekim.bankingapi.branch;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchRequest {

    @NotBlank(message = "Branch name is required")
    private String name;

    @NotBlank(message = "Branch code is required")
    private String code;

    @NotBlank(message = "City is required")
    private String city;

    private String address;
}