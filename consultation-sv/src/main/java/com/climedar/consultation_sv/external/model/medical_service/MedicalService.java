package com.climedar.consultation_sv.external.model.medical_service;

import lombok.Data;

import java.time.Duration;

@Data
public class MedicalService implements MedicalServices {
    private Long id;
    private String code;
    private Double price;
    private String name;
    private String description;
    private Duration estimatedDuration;
    private ServiceType serviceType;
    private String type = "service";

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }
}
