package com.climedar.medical_service_sv.dto.request;

import java.time.Duration;

public record UpdateMedicalServiceDTO(
        String name,
        String description,
        Double price,
        Duration estimatedDuration
) {
}
