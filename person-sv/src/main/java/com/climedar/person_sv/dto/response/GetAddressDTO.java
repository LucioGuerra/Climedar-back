package com.climedar.person_sv.dto.response;

public record GetAddressDTO(
        String street,
        String number,
        String floor,
        String apartment,
        String city,
        String province,
        String postalCode
) {
}
