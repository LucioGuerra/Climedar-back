package com.climedar.payment_sv.event;

import com.climedar.payment_sv.external.model.medical_services.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record PaymentEvent(
        BigDecimal amount,
        LocalDateTime date,
        ServiceType serviceType,
        String specialityName
) {

}
