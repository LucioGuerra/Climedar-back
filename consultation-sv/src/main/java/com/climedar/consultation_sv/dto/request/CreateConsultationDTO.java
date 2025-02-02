package com.climedar.consultation_sv.dto.request;

import java.time.Duration;
import java.time.LocalTime;

public record CreateConsultationDTO(
        Long patientId,
        Long medicalServices,
        String description,
        String observation,
        Long shiftId
) {
}
