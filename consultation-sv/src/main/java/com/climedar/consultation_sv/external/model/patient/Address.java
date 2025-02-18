package com.climedar.consultation_sv.external.model.patient;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Address {
    @Size(max = 100, message = "Street must be less than 100 characters")
    private String street;

    @Size(max = 10, message = "Number must be less than 10 characters")
    private String number;

    @Size(max = 5, message = "Floor must be less than 5 characters")
    private String floor;

    @Size(max = 5, message = "Apartment must be less than 5 characters")
    private String apartment;

    @Size(max = 50, message = "City must be less than 50 characters")
    private String city;

    @Size(max = 50, message = "Province must be less than 50 characters")
    private String province;

    @Size(max = 10, message = "Postal code must be less than 10 characters")
    private String postalCode;
}
