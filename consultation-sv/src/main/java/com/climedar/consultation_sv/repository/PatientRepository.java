package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.external.model.patient.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-sv")
public interface PatientRepository {

    @GetMapping("/api/public/patients/{id}")
    Patient findById(@PathVariable Long id);
}
