package com.climedar.doctor_sv.dto.request;

import com.climedar.doctor_sv.external.model.Gender;
import lombok.Data;

@Data
public class DoctorSpecificationDTO {
    private String name;
    private String surname;
    private String dni;
    private Long shiftId;
    private Long specialtyId;
    private Gender gender;
}
