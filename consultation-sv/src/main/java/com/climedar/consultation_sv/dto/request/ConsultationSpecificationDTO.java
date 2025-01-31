package com.climedar.consultation_sv.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultationSpecificationDTO(
        Long patientId,
        Long medicalServiceId,
        Long doctorId,
        String description,
        String observation,
        LocalDate date,
        LocalTime startTime,
        LocalTime fromTime,
        LocalTime toTime
) {
}
