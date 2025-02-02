package com.climedar.person_sv.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAddressDTO(
        @Size(max = 100, message = "Street must not exceed 100 characters")
        String street,

        @Size(max = 10, message = "Number must not exceed 10 characters")
        String number,

        @Size(max = 5, message = "Floor must not exceed 5 characters")
        String floor,

        @Size(max = 5, message = "Apartment must not exceed 5 characters")
        String apartment,

        @Size(max = 50, message = "City must not exceed 50 characters")
        String city,

        @Size(max = 50, message = "Province must not exceed 50 characters")
        String province,

        @Size(max = 10, message = "Postal code must not exceed 10 characters")
        String postalCode
) {
}
