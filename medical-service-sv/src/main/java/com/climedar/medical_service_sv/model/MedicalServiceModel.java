package com.climedar.medical_service_sv.model;

import com.climedar.medical_service_sv.entity.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MedicalServiceModel {
    private Long id;
    private String code;

    @NotBlank(message = "Name not be blank")
    private String name;

    @NotBlank(message = "Description not be blank")
    private String description;

    @NotNull(message = "ServiceType not be null")
    private ServiceType serviceType;

    @NotNull(message = "Price not be null")
    @Positive
    private Double price;

    @NotNull(message = "SpecialityID not be null")
    private Long specialityId;
}
