package com.climedar.payment_sv.external.model.medical_services;

import lombok.Data;

@Data
public class MedicalPackage implements MedicalServices {
    private Long id;
    private Double price;
}
