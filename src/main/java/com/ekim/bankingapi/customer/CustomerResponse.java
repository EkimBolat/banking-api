package com.ekim.bankingapi.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String nationalId;
    private Integer age;
    private String address;
    private Long branchId;
    private String branchName;
    private Integer naturePoints;
    private Integer treesPlanted;
    private LocalDateTime createdAt;

    public static CustomerResponse fromEntity(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getNationalId(),
                customer.getAge(),
                customer.getAddress(),
                customer.getBranch() != null ? customer.getBranch().getId() : null,
                customer.getBranch() != null ? customer.getBranch().getName() : null,
                customer.getNaturePoints(),
                customer.getTreesPlanted(),
                customer.getCreatedAt()
        );
    }
}