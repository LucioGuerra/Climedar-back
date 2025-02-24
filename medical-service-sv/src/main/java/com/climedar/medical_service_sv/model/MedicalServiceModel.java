package com.climedar.medical_service_sv.model;

import com.climedar.medical_service_sv.entity.ServiceType;
import com.climedar.medical_service_sv.external.model.Speciality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.Duration;

@Data
public class MedicalServiceModel implements MedicalServicesModel {
    private Long id;
    private String code;

    @NotBlank(message = "Name not be blank")
    private String name;

    @NotBlank(message = "Description not be blank")
    private String description;

    private Duration estimatedDuration;

    @NotNull(message = "ServiceType not be null")
    private ServiceType serviceType;

    @NotNull(message = "Price not be null")
    @Positive
    private Double price;

    @NotNull(message = "Speciality not be null")
    private Speciality speciality;

    private String type = "service";
}
