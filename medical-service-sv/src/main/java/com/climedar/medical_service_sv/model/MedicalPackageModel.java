package com.climedar.medical_service_sv.model;

import lombok.Data;

import java.time.Duration;
import java.util.Set;

@Data
public class MedicalPackageModel implements MedicalServicesModel {
    private Long id;
    private String code;
    private String name;
    private Duration estimatedDuration;
    private Set<MedicalServiceModel> services;
    private Double price;

    public MedicalPackageModel(Long id) {
        this.id = id;
    }

    public MedicalPackageModel(){

    }
}
