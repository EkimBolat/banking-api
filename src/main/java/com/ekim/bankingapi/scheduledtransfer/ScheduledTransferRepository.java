package com.ekim.bankingapi.scheduledtransfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduledTransferRepository extends JpaRepository<ScheduledTransfer, Long> {

    List<ScheduledTransfer> findByFromAccountCustomerId(Long customerId);

    List<ScheduledTransfer> findByActiveTrueAndNextExecutionDateLessThanEqual(LocalDate date);
}