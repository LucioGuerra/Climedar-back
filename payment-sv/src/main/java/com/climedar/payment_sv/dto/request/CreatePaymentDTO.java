package com.climedar.payment_sv.dto.request;

import com.climedar.payment_sv.entity.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentDTO(
        Long consultationId,
        PaymentMethod paymentMethod,
        BigDecimal amount
) {
}
