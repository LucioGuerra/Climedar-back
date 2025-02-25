package com.climedar.payment_sv.external.model.medical_services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MedicalPackage implements MedicalServices {
    private Long id;
    private BigDecimal price;
    private String name;
    private String code;

    @JsonProperty("services")
    private List<MedicalService> medicalServices;
}
