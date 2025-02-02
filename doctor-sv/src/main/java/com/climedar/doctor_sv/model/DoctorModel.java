package com.climedar.doctor_sv.model;

import com.climedar.doctor_sv.external.model.Address;
import com.climedar.doctor_sv.external.model.Person;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DoctorModel extends Person {
    private Long id;

    @Positive(message = "Salary must be positive")
    private Double salary;

    @Valid
    private SpecialityModel speciality;

    @Valid
    private List<ShiftModel> shifts;

}
