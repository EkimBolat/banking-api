package com.ekim.bankingapi.branch;

import com.ekim.bankingapi.exception.DuplicateResourceException;
import com.ekim.bankingapi.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public BranchResponse createBranch(BranchRequest request) {
        if (branchRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Branch code already in use: " + request.getCode());
        }

        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setCode(request.getCode());
        branch.setCity(request.getCity());
        branch.setAddress(request.getAddress());

        Branch saved = branchRepository.save(branch);
        return BranchResponse.fromEntity(saved);
    }

    public BranchResponse getBranchById(Long id) {
        Branch branch = findBranchEntityById(id);
        return BranchResponse.fromEntity(branch);
    }

    public Page<BranchResponse> getAllBranches(Pageable pageable) {
        return branchRepository.findAll(pageable)
                .map(BranchResponse::fromEntity);
    }

    // Customer feature'ı, şubeyi entity olarak kullanacağı için bunu koruyoruz
    public Branch findBranchEntityById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
    }
}