package com.climedar.consultation_sv.external.model.doctor;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Doctor {
    private Long id;
    private Speciality speciality;
    private String name;
    private String surname;
    private String dni;
    private Double salary;
    private String phone;
}
