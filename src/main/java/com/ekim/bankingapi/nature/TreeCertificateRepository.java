package com.ekim.bankingapi.nature;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeCertificateRepository extends JpaRepository<TreeCertificate, Long> {

    List<TreeCertificate> findByCustomerIdOrderByPlantedAtDesc(Long customerId);

    long count();
}