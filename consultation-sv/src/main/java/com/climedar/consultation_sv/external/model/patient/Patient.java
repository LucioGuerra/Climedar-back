package com.climedar.consultation_sv.external.model.patient;

import com.climedar.consultation_sv.external.model.doctor.Speciality;
import lombok.Data;

@Data
public class Patient {
    private Long id;
    private String name;
    private String surname;
    private String dni;
    private String phone;
    private String email;
    private MedicalSecure medicalSecure;
    private Address address;


    public Patient() {
    }

    public Patient(Long id) {
        this.id = id;
    }
}

