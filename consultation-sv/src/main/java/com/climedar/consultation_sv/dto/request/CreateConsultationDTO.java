package com.climedar.consultation_sv.dto.request;

import java.time.Duration;
import java.time.LocalTime;

public record CreateConsultationDTO(
        Long patientId,
        Long medicalServiceId,
        Duration estimatedDuration,
        LocalTime startTime,
        String description,
        String observation,
        Long shiftId
) {
}
