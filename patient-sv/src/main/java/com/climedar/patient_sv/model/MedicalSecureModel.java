package com.climedar.patient_sv.model;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class MedicalSecureModel {
    private Long id;

    @Valid
    private String nombre;
}
