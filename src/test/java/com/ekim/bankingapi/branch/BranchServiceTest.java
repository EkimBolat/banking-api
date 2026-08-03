package com.ekim.bankingapi.branch;

import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchService branchService;

    private BranchRequest request;

    @BeforeEach
    void setUp() {
        request = new BranchRequest();
        request.setName("Kadıköy Şubesi");
        request.setCode("1001");
        request.setCity("İstanbul");
        request.setAddress("Bahariye Cad. No:5");
    }

    @Test
    void createBranch_shouldSucceed_whenCodeIsUnique() {
        when(branchRepository.existsByCode("1001")).thenReturn(false);
        when(branchRepository.save(any(Branch.class))).thenAnswer(invocation -> {
            Branch b = invocation.getArgument(0);
            b.setId(1L);
            return b;
        });

        BranchResponse response = branchService.createBranch(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Kadıköy Şubesi");
        assertThat(response.getCode()).isEqualTo("1001");
    }

    @Test
    void createBranch_shouldThrow_whenCodeAlreadyExists() {
        when(branchRepository.existsByCode("1001")).thenReturn(true);

        assertThatThrownBy(() -> branchService.createBranch(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already in use");

        verify(branchRepository, never()).save(any());
    }

    @Test
    void getBranchById_shouldReturnBranch_whenExists() {
        Branch branch = new Branch();
        branch.setId(1L);
        branch.setName("Kadıköy Şubesi");
        branch.setCode("1001");

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));

        BranchResponse response = branchService.getBranchById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Kadıköy Şubesi");
    }

    @Test
    void getBranchById_shouldThrow_whenNotFound() {
        when(branchRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> branchService.getBranchById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}