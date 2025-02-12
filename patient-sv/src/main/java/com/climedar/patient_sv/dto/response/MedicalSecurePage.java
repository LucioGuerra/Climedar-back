package com.climedar.patient_sv.dto.response;

import com.climedar.library.dto.response.PageInfo;
import com.climedar.patient_sv.model.MedicalSecureModel;
import com.climedar.patient_sv.model.PatientModel;
import lombok.Data;

import java.util.List;

@Data
public class MedicalSecurePage {
    private PageInfo pageInfo;
    private List<MedicalSecureModel> medicalSecures;
}
