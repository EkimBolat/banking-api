package com.ekim.bankingapi.nature;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TreeCertificateResponse {

    private Long id;
    private Long customerId;
    private String customerFullName;
    private String certificateNumber;
    private TreeSpecies species;
    private String plantingRegion;
    private LocalDateTime plantedAt;

    public static TreeCertificateResponse fromEntity(TreeCertificate certificate) {
        return new TreeCertificateResponse(
                certificate.getId(),
                certificate.getCustomer().getId(),
                certificate.getCustomer().getFirstName() + " " + certificate.getCustomer().getLastName(),
                certificate.getCertificateNumber(),
                certificate.getSpecies(),
                certificate.getPlantingRegion(),
                certificate.getPlantedAt()
        );
    }
}