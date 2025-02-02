package com.climedar.person_sv.dto.response;

import com.climedar.person_sv.entity.Gender;

import java.time.LocalDate;

public record GetPersonDTO(
        Long personId,
        String name,
        String surname,
        String dni,
        LocalDate birthdate,
        String email,
        String phone,
        Gender gender,
        GetAddressDTO address,
        Boolean deleted
) {
}
