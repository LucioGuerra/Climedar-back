package com.climedar.payment_sv.external.model.medical_services;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

public interface MedicalServices {

    Long getId();

    BigDecimal getPrice();

    String getName();

    String getCode();
}
