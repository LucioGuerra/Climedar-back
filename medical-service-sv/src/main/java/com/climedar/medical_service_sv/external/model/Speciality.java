package com.climedar.medical_service_sv.external.model;

import lombok.Data;

@Data
public class Speciality {
    private Long id;
    private String code;
    private String name;
    private String description;
}
