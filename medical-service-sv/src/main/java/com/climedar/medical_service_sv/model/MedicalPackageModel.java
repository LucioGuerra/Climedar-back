package com.climedar.medical_service_sv.model;

import com.climedar.medical_service_sv.external.model.Speciality;
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
    private Speciality speciality;

    private String type = "package";

    public MedicalPackageModel(Long id) {
        this.id = id;
    }

    public MedicalPackageModel(){

    }
}
