package com.climedar.consultation_sv.external.model.medical_service;

import lombok.Data;

import java.time.Duration;

@Data
public class MedicalServiceModel implements MedicalServicesModel {
    private Long id;
    private String code;
    private Double price;
    private String name;
    private String description;
    private Duration estimatedDuration;
    private ServiceType serviceType;

    public MedicalServiceModel(String code) {
        this.code = code;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }
}
