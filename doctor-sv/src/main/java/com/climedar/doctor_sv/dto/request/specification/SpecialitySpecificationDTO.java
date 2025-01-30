package com.climedar.doctor_sv.dto.request.specification;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SpecialitySpecificationDTO {
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Size(min = 1, max = 150, message = "Description must be between 1 and 150 characters")
    private String description;

    private String code;
}

