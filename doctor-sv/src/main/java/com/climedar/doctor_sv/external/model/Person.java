package com.climedar.doctor_sv.external.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class Person { //todo: cambiar estos protected por private
    protected Long personId;
    protected String name;
    protected String surname;
    protected String dni;
    protected String email;
    protected String phone;
    protected String birthdate;
    protected Address address;
    protected Gender gender;


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
