package com.climedar.doctor_sv.model;

import com.climedar.doctor_sv.external.model.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DoctorModel extends Person {
    private Long id;
    private Double salary;
    private SpecialityModel speciality;
    private List<ShiftModel> shifts;
}
