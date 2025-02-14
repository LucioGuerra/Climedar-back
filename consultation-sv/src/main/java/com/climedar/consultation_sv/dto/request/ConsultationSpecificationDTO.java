package com.climedar.consultation_sv.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ConsultationSpecificationDTO{
    private Long patientId;
    private List<String> medicalServiceCodes;
    private Long doctorId;
    private String description;
    private String observation;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime fromTime;
    private LocalTime toTime;
}
