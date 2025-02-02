package com.climedar.consultation_sv.dto.request;

import com.climedar.consultation_sv.external.model.medical_service.MedicalPackage;
import com.climedar.consultation_sv.external.model.medical_service.MedicalService;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServices;
import lombok.Data;
import lombok.Setter;

@Setter
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
