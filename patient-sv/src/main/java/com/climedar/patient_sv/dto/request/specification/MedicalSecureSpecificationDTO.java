package com.climedar.patient_sv.dto.request.specification;

import com.climedar.patient_sv.external.model.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MedicalSecureSpecificationDTO {
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;
}
