package com.climedar.consultation_sv.model;

import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServicesModel;
import com.climedar.consultation_sv.external.model.patient.Patient;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class ConsultationModel {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private Duration estimatedDuration;
    private String description;
    private Double finalPrice;
    private String observation;
    private Doctor doctor;
    private Patient patient;
    private List<MedicalServicesModel> medicalServicesModel;
}
