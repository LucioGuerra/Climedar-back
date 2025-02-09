package com.climedar.medical_service_sv.model;

import java.time.Duration;

public interface MedicalServicesModel {

    Long getId();
    String getCode();
    Double getPrice();
    Duration getEstimatedDuration();
}
