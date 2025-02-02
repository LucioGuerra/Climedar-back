package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.external.model.patient.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "patient-sv")
public interface PatientRepository {

    @GetMapping("/api/public/patients/{id}")
    Patient findById(@PathVariable Long id);

    @GetMapping("/api/public/patients/ids")
    List<Patient> findAllById(@RequestParam Set<Long> ids);
}
