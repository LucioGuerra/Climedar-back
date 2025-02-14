package com.climedar.person_sv.dto.request.create;

import com.climedar.person_sv.entity.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreatePersonDTO{
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        private String name;

        @NotBlank(message = "Surname cannot be empty")
        @Size(max = 50, message = "Surname must not exceed 50 characters")
        private String surname;

        @NotBlank(message = "DNI cannot be empty")
        @Pattern(regexp = "^[0-9]{7,8}$", message = "DNI must contain 7 or 8 numeric digits")
        private String dni;

        @NotNull(message = "Birthdate cannot be null")
        @Past(message = "Birthdate must be a date in the past")
        private LocalDate birthdate;

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email must be a valid email address")
        private String email;

        @NotBlank(message = "Phone cannot be empty")
        @Size(max = 25, message = "Phone must not exceed 25 characters")
        private String phone;

        @NotNull(message = "Gender cannot be null")
        private Gender gender;

        @NotNull(message = "Address cannot be null")
        @Valid
        private CreateAddressDTO address;
}

