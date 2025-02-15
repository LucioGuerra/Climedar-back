package com.climedar.medical_service_sv.dto.request;

import com.climedar.medical_service_sv.model.MedicalServiceModel;

import java.util.Set;

public record CreatePackageDTO(
        String name,
        Set<Long> servicesIds,
        Long specialityId
) {
}
