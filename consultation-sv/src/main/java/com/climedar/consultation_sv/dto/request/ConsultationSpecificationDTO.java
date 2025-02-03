package com.climedar.consultation_sv.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ConsultationSpecificationDTO{
    private Long patientId;
    private Long medicalServiceId;
    private Long doctorId;
    private String description;
    private String observation;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime fromTime;
    private LocalTime toTime;
}
