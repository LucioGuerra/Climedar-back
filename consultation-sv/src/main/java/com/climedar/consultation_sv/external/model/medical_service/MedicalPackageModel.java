package com.climedar.consultation_sv.external.model.medical_service;

import com.climedar.consultation_sv.external.model.doctor.Speciality;
import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
public class MedicalPackageModel implements MedicalServicesModel {
    private Long id;
    private String code;
    private Double price;
    private List<MedicalServiceModel> services;
    private Duration estimatedDuration;
    private Speciality speciality;


    public MedicalPackageModel(String code) {
        this.code = code;
    }

    @Override
    public Double getPrice() {
        double sumPrice = this.services.stream().mapToDouble(MedicalServiceModel::getPrice).sum();
        return sumPrice * 0.85;
    }

    @Override
    public Duration getEstimatedDuration() {
        long sumDuration = this.services.stream().mapToLong(service -> service.getEstimatedDuration().toMinutes()).sum();
        return Duration.ofMinutes(sumDuration);
    }
}
