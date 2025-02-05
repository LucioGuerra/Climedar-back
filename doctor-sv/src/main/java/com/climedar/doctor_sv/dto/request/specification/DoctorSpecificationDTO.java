package com.climedar.doctor_sv.dto.request.specification;

import com.climedar.doctor_sv.external.model.Gender;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DoctorSpecificationDTO {
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Size(min = 1, max = 50, message = "Full Name must be between 1 and 50 characters")
    private String fullName;

    @Size(min = 1, max = 50, message = "Surname must be between 1 and 50 characters")
    private String surname;

    @Size(min = 7, max = 8, message = "DNI must be between 7 and 8 characters")
    private String dni;

    private Long shiftId;
    private Long specialityId;
    private Gender gender;
}
