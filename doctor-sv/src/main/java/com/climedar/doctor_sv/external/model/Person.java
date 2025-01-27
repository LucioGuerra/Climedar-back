package com.climedar.doctor_sv.external.model;

import lombok.AllArgsConstructor;
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
    private boolean deleted;



    public Person() {
    }

    public Person(String name, String surname, String dni, String email,
                  String phone, String birthdate, Address address, Gender gender) {
        this.name = name;
        this.surname = surname;
        this.dni = dni;
        this.email = email;
        this.phone = phone;
        this.birthdate = birthdate;
        this.address = address;
        this.gender = gender;
    }

}
