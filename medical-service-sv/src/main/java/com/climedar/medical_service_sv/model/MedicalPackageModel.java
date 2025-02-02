package com.climedar.medical_service_sv.model;

import lombok.Data;

import java.util.Set;

@Data
public class MedicalPackageModel {
    private Long id;
    private String code;
    private String name;
    private Set<MedicalServiceModel> services;
    private Double price;
}
