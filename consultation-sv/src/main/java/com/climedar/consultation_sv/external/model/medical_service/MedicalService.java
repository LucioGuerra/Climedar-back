package com.climedar.consultation_sv.external.model.medical_service;

import lombok.Data;

@Data
public class MedicalService implements MedicalServices {
    private Long id;
    private String code;
    private Double price;
    private String name;
    private String description;
    private ServiceType serviceType;

    @Override
    public Double getPrice() {
        return price;
    }
}
