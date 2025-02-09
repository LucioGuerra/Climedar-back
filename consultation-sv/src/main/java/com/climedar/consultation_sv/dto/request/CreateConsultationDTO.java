package com.climedar.consultation_sv.dto.request;

public record CreateConsultationDTO(
        Long patientId,
        Long medicalServicesId,
        String description,
        String observation,
        Long shiftId
) {
}
