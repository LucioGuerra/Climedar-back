package com.climedar.patient_sv.dto.response;

import com.climedar.library.dto.response.PageInfo;
import com.climedar.patient_sv.model.PatientModel;
import lombok.Data;

import java.util.List;

@Data
public class PatientPage {
    private PageInfo pageInfo;
    private List<PatientModel> patients;
}
