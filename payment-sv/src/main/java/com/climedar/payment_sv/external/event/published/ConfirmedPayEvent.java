package com.climedar.payment_sv.external.event.published;

import lombok.Data;

@Data
public class ConfirmedPayEvent {
    private Long consultationId;

    public ConfirmedPayEvent(Long consultationId) {
        this.consultationId = consultationId;

    }
}
