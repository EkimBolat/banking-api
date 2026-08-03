package com.ekim.bankingapi.nature;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NatureServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private TreeCertificateRepository treeCertificateRepository;

    @InjectMocks
    private NatureService natureService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setNaturePoints(0);
        customer.setTreesPlanted(0);
    }

    @Test
    void awardPoints_shouldDoNothing_whenAmountBelowMinimum() {
        natureService.awardPointsForTransaction(1L, BigDecimal.valueOf(30));

        verify(customerService, never()).findCustomerEntityById(any());
    }

    @Test
    void awardPoints_shouldIncreasePoints_whenAmountAboveMinimum() {
        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);

        natureService.awardPointsForTransaction(1L, BigDecimal.valueOf(100));

        assertThat(customer.getNaturePoints()).isEqualTo(5);
        assertThat(customer.getTreesPlanted()).isEqualTo(0);
        verify(customerService).saveCustomerEntity(customer);
    }

    @Test
    void awardPoints_shouldPlantTree_whenReaching100Points() {
        customer.setNaturePoints(95);
        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);
        when(treeCertificateRepository.count()).thenReturn(0L);

        natureService.awardPointsForTransaction(1L, BigDecimal.valueOf(100));

        assertThat(customer.getTreesPlanted()).isEqualTo(1);
        assertThat(customer.getNaturePoints()).isEqualTo(0);

        ArgumentCaptor<TreeCertificate> captor = ArgumentCaptor.forClass(TreeCertificate.class);
        verify(treeCertificateRepository).save(captor.capture());
        assertThat(captor.getValue().getCustomer()).isEqualTo(customer);
        assertThat(captor.getValue().getCertificateNumber()).startsWith("TREE-");
    }

    @Test
    void awardPoints_shouldRespectDailyCap() {
        customer.setDailyNaturePoints(48);
        customer.setLastPointsDate(java.time.LocalDate.now());
        when(customerService.findCustomerEntityById(1L)).thenReturn(customer);

        natureService.awardPointsForTransaction(1L, BigDecimal.valueOf(100));

        assertThat(customer.getDailyNaturePoints()).isEqualTo(50);
        assertThat(customer.getNaturePoints()).isEqualTo(2);
    }

    @Test
    void getTotalTreesPlanted_shouldReturnRepositoryCount() {
        when(treeCertificateRepository.count()).thenReturn(42L);

        long total = natureService.getTotalTreesPlanted();

        assertThat(total).isEqualTo(42L);
    }
}