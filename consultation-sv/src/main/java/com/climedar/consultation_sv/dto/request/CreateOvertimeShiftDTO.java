package com.climedar.consultation_sv.dto.request;

public record CreateOvertimeShiftDTO(
        Long doctorId,
        String date,
        String startTime,
        String endTime,
        String place
) {
}
