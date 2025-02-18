package com.climedar.medical_service_sv.dto.request;

import java.util.Set;

public record UpdatePackageDTO(
        String name,
        Set<Long> servicesIds
) {
}
