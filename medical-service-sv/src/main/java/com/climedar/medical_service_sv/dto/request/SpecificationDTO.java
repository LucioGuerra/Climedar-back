package com.climedar.medical_service_sv.dto.request;

import com.climedar.medical_service_sv.entity.ServiceType;
import lombok.Data;

@Data
public class SpecificationDTO {
    private String name;
    private String code;
    private String description;
    private ServiceType serviceType;
    private Long specialityId;
}
