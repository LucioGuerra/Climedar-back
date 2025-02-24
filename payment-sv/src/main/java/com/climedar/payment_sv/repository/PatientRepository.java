package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.external.model.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-sv")
public interface PatientRepository {
    @GetMapping("/api/patients/{id}")
    Patient getPatientById(@PathVariable Long id);

}
