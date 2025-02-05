package com.climedar.payment_sv.event.internal;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public record PaymentEvent(
        BigDecimal amount,
        LocalDateTime date
) {

}
