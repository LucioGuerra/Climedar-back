package com.climedar.doctor_sv.external.event;

import lombok.Data;

@Data
public class ShiftCanceledEvent {
    private Long shiftId;

    public ShiftCanceledEvent(Long shiftId) {
        this.shiftId = shiftId;
    }
}
