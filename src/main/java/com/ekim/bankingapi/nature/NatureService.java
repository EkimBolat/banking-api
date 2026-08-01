package com.ekim.bankingapi.nature;

import com.ekim.bankingapi.customer.Customer;
import com.ekim.bankingapi.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class NatureService {

    private static final int POINTS_PER_TRANSACTION = 5;
    private static final int POINTS_PER_TREE = 100;

    private static final String[] PLANTING_REGIONS = {
            "Karadeniz Ormanları",
            "Ege Makilikleri",
            "İç Anadolu Bozkırı",
            "Akdeniz Kıyı Şeridi",
            "Doğu Anadolu Yaylaları"
    };

    private static final Random RANDOM = new Random();

    private final CustomerService customerService;
    private final TreeCertificateRepository treeCertificateRepository;

    public void awardPointsForTransaction(Long customerId) {
        Customer customer = customerService.findCustomerEntityById(customerId);

        int totalPoints = customer.getNaturePoints() + POINTS_PER_TRANSACTION;

        while (totalPoints >= POINTS_PER_TREE) {
            totalPoints -= POINTS_PER_TREE;
            customer.setTreesPlanted(customer.getTreesPlanted() + 1);
            plantTree(customer);
        }

        customer.setNaturePoints(totalPoints);
        customerService.saveCustomerEntity(customer);
    }

    public List<TreeCertificateResponse> getCertificatesForCustomer(Long customerId) {
        return treeCertificateRepository.findByCustomerIdOrderByPlantedAtDesc(customerId).stream()
                .map(TreeCertificateResponse::fromEntity)
                .toList();
    }

    public long getTotalTreesPlanted() {
        return treeCertificateRepository.count();
    }

    private void plantTree(Customer customer) {
        TreeCertificate certificate = new TreeCertificate();
        certificate.setCustomer(customer);
        certificate.setCertificateNumber(generateCertificateNumber());
        certificate.setSpecies(pickRandomSpecies());
        certificate.setPlantingRegion(pickRandomRegion());
        treeCertificateRepository.save(certificate);

        log.info("Tree planted! customerId={}, certificateNumber={}, species={}, region={}",
                customer.getId(), certificate.getCertificateNumber(), certificate.getSpecies(), certificate.getPlantingRegion());
    }

    private String generateCertificateNumber() {
        long sequence = treeCertificateRepository.count() + 1;
        return String.format("TREE-%d-%06d", Year.now().getValue(), sequence);
    }

    private TreeSpecies pickRandomSpecies() {
        TreeSpecies[] species = TreeSpecies.values();
        return species[RANDOM.nextInt(species.length)];
    }

    private String pickRandomRegion() {
        return PLANTING_REGIONS[RANDOM.nextInt(PLANTING_REGIONS.length)];
    }
}