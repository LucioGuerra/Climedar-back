package com.climedar.payment_sv.external.model;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String number;
    private String province = "La Plata";
}
