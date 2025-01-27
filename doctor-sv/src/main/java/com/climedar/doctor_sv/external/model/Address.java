package com.climedar.doctor_sv.external.model;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String number;
    private String floor;
    private String apartment;
    private String city;
    private String province;
    private String postalCode;
}
