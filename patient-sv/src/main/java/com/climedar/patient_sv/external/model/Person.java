package com.climedar.patient_sv.external.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


@Data
public class Person {
    private Long personId;

    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Size(min = 1, max = 50, message = "Surname must be between 1 and 50 characters")
    private String surname;

    @Size(min = 7, max = 8, message = "DNI must be between 7 and 8 characters")
    private String dni;

    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 7, max = 25, message = "Phone must be between 7 and 25 characters")
    private String phone;

    @PastOrPresent(message = "Birthdate must be in the past or present")
    private String birthdate;

    @Valid
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
