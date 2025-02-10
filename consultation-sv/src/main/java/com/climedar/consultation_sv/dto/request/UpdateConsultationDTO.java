package com.climedar.consultation_sv.dto.request;


import java.util.List;
import java.util.Set;

public record UpdateConsultationDTO(
        Set<Long> medicalServicesId,
        String description,
        String observation,
        Long shiftId
) {
}
