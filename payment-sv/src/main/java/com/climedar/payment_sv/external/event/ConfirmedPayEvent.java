package com.climedar.payment_sv.external.event;

import lombok.Data;

@Data
public class ConfirmedPayEvent {
    private Long consultationId;

    public ConfirmedPayEvent(Long consultationId) {
        this.consultationId = consultationId;

    }
}
