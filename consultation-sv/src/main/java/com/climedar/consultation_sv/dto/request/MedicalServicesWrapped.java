package com.climedar.consultation_sv.dto.request;

import com.climedar.consultation_sv.external.model.medical_service.MedicalPackageModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServiceModel;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import lombok.Setter;

@Setter
public class MedicalServicesWrapped {
    private MedicalServiceModel medicalServiceModel;
    private MedicalPackageModel medicalPackageModel;

    public MedicalServicesModel getMedicalServices() {
        if (medicalServiceModel != null) {
            return this.medicalServiceModel;
        } else {
            return medicalPackageModel;
        }
    }
}
