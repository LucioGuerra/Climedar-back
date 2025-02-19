package com.climedar.payment_sv.external.model;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String number;
    private String apartment;
    private String floor;
    private String province;


    public String toString(){
        if (apartment != null && floor != null){
            return street + " " + number + ", " + floor + ", " + apartment;
        }
        return street + " " + number;
    }
}
