package com.climedar.medical_service_sv.dto.request;

import com.climedar.medical_service_sv.entity.ServiceType;

public record CreateMedicalDTO(
        String name,
        String description,
        Float price,
        ServiceType serviceType,
        Long specialityId,
        String estimatedDuration
) {
}
