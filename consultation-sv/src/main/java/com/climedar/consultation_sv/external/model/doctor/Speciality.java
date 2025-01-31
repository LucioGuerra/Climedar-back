package com.climedar.consultation_sv.external.model.doctor;

import lombok.Data;

@Data
public class Speciality {
    private Long id;
    private String code;
    private String name;
    private String description;
}
