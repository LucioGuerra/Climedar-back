package com.climedar.medical_service_sv.dto.response;

import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import lombok.Data;

@Data
public class MedicalServicesWrapped {
    private MedicalServiceModel medicalService;
    private MedicalPackageModel medicalPackage;
}
