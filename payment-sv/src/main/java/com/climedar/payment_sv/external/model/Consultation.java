package com.climedar.payment_sv.external.model;

import com.climedar.payment_sv.external.model.medical_services.MedicalServices;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Consultation {
    Long id;
    Patient patient;
    BigDecimal finalPrice;
    List<MedicalServices> medicalServicesModel;

    public Consultation(Long id) {
        this.id = id;
    }
}
