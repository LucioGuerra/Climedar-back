package com.climedar.doctor_sv.external.model;

import lombok.Data;

@Data
public class Person {
    private Long personId;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phone;
    private String birthdate;
    private Address address;
    private Gender gender;

    private Long getPersonId() {
        return personId;
    }
}
