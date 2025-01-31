package com.climedar.consultation_sv.external.model.medical_service;

import lombok.Data;

import java.util.List;

@Data
public class MedicalPackage implements MedicalServices {
    private Long id;
    private String code;
    private Double price;
    private List<MedicalService> services;

    @Override
    public Double getPrice() {
        double sumPrice = this.services.stream().mapToDouble(MedicalService::getPrice).sum();
        return sumPrice * 0.85;
    }

}
