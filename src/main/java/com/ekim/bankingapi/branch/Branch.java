package com.ekim.bankingapi.branch;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Branch name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Branch code is required")
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @NotBlank(message = "City is required")
    @Column(nullable = false)
    private String city;

    private String address;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}