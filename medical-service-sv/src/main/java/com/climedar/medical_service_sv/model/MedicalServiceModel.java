package com.climedar.medical_service_sv.model;

import com.climedar.medical_service_sv.entity.ServiceType;
import lombok.Data;

@Data
public class MedicalServiceModel {//todo: agregar validaciones
    private Long id;
    private String code;
    private String name;
    private String description;
    private ServiceType serviceType;
    private Double price;
}
