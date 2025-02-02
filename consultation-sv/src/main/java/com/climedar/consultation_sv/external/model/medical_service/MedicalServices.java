package com.climedar.consultation_sv.external.model.medical_service;

import java.time.Duration;

public interface MedicalServices {
    Long getId();

    Double getPrice();

    Duration getEstimatedDuration();
}
