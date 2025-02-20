package com.climedar.payment_sv.external.model.medical_services;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MedicalService.class, name = "service"),
        @JsonSubTypes.Type(value = MedicalPackage.class, name = "package")
})
public interface MedicalServices {

    Long getId();

    BigDecimal getPrice();

    String getName();

    String getCode();
}
