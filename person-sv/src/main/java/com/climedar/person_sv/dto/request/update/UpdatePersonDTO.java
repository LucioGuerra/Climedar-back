package com.climedar.person_sv.dto.request.update;

import com.climedar.person_sv.entity.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdatePersonDTO(
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Size(max = 50, message = "Surname must not exceed 50 characters")
        String surname,

        @Pattern(regexp = "^[0-9]{7,8}$", message = "DNI must contain 7 or 8 numeric digits")
        String dni,

        @Past(message = "Birthdate must be a date in the past")
        LocalDate birthdate,

        @Email(message = "Email must be a valid email address")
        String email,

        @Size(max = 25, message = "Phone must not exceed 25 characters")
        String phone,

        @NotNull(message = "Gender cannot be null")
        Gender gender,

        @Valid
        UpdateAddressDTO address
) {
}
