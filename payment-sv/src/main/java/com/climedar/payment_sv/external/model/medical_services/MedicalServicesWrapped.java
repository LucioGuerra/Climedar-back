package com.climedar.payment_sv.external.model.medical_services;

import lombok.Data;

@Data
public class MedicalServicesWrapped {
    private MedicalService medicalService;
    private MedicalPackage medicalPackage;


    public MedicalServices getMedicalServices() {
        if (medicalService != null) {
            return medicalService;
        } else {
            return medicalPackage;
        }
    }
}
