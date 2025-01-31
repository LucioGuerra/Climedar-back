package com.climedar.consultation_sv.dto.request;

import java.time.Duration;
import java.time.LocalTime;

public record updateConsultationDTO(
        Long medicalServiceId,
        Duration estimatedDuration, //todo: deberia ser parte del servicio
        LocalTime startTime,
        String description,
        String observation,
        Long shiftId
) {
}
