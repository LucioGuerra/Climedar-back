package com.climedar.medical_service_sv.dto.request;

public record UpdateMedicalServiceDTO(
        String name,
        String description,
        Double price
) {
}
