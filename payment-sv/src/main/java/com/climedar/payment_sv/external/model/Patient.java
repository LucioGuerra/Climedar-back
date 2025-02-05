package com.climedar.payment_sv.external.model;

import lombok.Data;

@Data
public class Patient {
    private Long id;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phone;
    private String address;

}
