package com.climedar.payment_sv.event;

import com.climedar.payment_sv.external.model.medical_services.ServicesType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public record PaymentEvent(
        BigDecimal amount,
        LocalDateTime date,
        ServicesType servicesType,
        String specialityName
) {

}
