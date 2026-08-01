package com.ekim.bankingapi.nature;

import com.ekim.bankingapi.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tree_certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreeCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, unique = true)
    private String certificateNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TreeSpecies species;

    @Column(nullable = false)
    private String plantingRegion;

    @Column(updatable = false)
    private LocalDateTime plantedAt;

    @PrePersist
    protected void onCreate() {
        this.plantedAt = LocalDateTime.now();
    }
}