package com.climedar.consultation_sv.external.event.received;

import lombok.Data;

@Data
public class ShiftCanceledEvent {
    private Long shiftId;
}
