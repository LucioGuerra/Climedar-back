package com.climedar.patient_sv.model;

import com.climedar.patient_sv.external.model.Person;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PatientModel extends Person {
    private Long id;

    @Valid
    private MedicalSecureModel medicalSecure;

}
