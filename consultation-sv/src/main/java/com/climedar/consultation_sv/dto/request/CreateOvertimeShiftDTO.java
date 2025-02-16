package com.climedar.consultation_sv.dto.request;

import java.time.Duration;

public record CreateOvertimeShiftDTO(
        Long doctorId,
        Duration timeOfShifts
) {
}
