package com.climedar.consultation_sv.external.model.medical_service;

import com.climedar.consultation_sv.external.model.doctor.Speciality;

import java.time.Duration;

public interface MedicalServicesModel {
    Long getId();

    Double getPrice();

    Duration getEstimatedDuration();

    String getCode();

    Speciality getSpeciality();
}
