package com.ekim.bankingapi.branch;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BranchResponse {

    private Long id;
    private String name;
    private String code;
    private String city;
    private String address;
    private LocalDateTime createdAt;

    public static BranchResponse fromEntity(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getCode(),
                branch.getCity(),
                branch.getAddress(),
                branch.getCreatedAt()
        );
    }
}