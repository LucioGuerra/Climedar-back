package com.climedar.payment_sv.dto.request;

import com.climedar.payment_sv.entity.revenue.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServicesType;

public record RevenueSpecificationDTO(
        String date,
        String fromDate,
        String toDate,
        RevenueType revenueType,
        String specialityName,
        ServicesType servicesType,
        OriginName originName
) {
}
