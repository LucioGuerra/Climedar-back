package com.climedar.payment_sv.external.model.medical_services;

import lombok.Data;

@Data
public class MedicalService implements MedicalServices{
    private Long id;
    private Double price;
}
