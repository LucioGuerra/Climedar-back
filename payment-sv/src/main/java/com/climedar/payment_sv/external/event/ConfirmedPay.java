package com.climedar.payment_sv.external.event;

public record ConfirmedPay(
        Long consultationId,
        Boolean isPaid
) {
}
