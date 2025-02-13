package com.climedar.payment_sv.external.model.medical_services;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MedicalPackage implements MedicalServices {
    private Long id;
    private BigDecimal price;
    private String name;
    private String code;
    private List<MedicalService> medicalServices;
}
