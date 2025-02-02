package com.climedar.consultation_sv.dto.request;

import java.time.Duration;
import java.time.LocalTime;

public record UpdateConsultationDTO(
        Long medicalServiceId,
        String description,
        String observation,
        Long shiftId
) {
}
