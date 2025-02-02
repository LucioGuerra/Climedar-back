package com.climedar.medical_service_sv.dto.response;

import com.climedar.medical_service_sv.model.MedicalServiceModel;
import lombok.Data;

import java.util.List;

@Data
public class MedicalServicePage {
    private PageInfo pageInfo;
    private List<MedicalServiceModel> services;
}
