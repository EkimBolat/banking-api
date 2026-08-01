package com.ekim.bankingapi.account;

import com.ekim.bankingapi.branch.Branch;
import com.ekim.bankingapi.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String accountNumber;

    @NotNull
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private AccountType accountType;

    private BigDecimal interestRate;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(nullable = false)
    private BigDecimal dailyLimit = BigDecimal.valueOf(50000);

    @Column(nullable = false)
    private BigDecimal monthlyLimit = BigDecimal.valueOf(500000);

    @Column(nullable = false)
    private BigDecimal dailyWithdrawnAmount = BigDecimal.ZERO;

    private LocalDate lastWithdrawalDate;

    @Column(nullable = false)
    private BigDecimal monthlyWithdrawnAmount = BigDecimal.ZERO;

    private String lastWithdrawalMonth;

    @NotNull
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}