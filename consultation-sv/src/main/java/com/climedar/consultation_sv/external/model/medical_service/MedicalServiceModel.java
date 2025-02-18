package com.climedar.consultation_sv.external.model.medical_service;

import com.climedar.consultation_sv.external.model.doctor.Speciality;
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
    private String type = "service";
    private Speciality speciality;

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
