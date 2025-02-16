package com.climedar.consultation_sv.dto.request;

import java.util.Set;

public record CreateOvertimeConsultationDTO(
        Long patientId,
        Set<Long> medicalServicesId,
        String description,
        String observation,
        CreateOvertimeShiftDTO shift
) {

}
