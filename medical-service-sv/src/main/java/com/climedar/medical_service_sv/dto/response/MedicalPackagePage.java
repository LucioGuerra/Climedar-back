package com.climedar.medical_service_sv.dto.response;

import com.climedar.medical_service_sv.model.MedicalPackageModel;
import lombok.Data;

import java.util.List;

@Data
public class MedicalPackagePage {
    private PageInfo pageInfo;
    private List<MedicalPackageModel> medicalPackages;
}
