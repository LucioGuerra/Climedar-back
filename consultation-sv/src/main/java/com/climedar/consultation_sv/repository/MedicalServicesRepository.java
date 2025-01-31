package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.external.model.medical_service.MedicalServices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medical-sv")
public interface MedicalServicesRepository {
    @GetMapping("/api/public/medical-services/{id}")
    MedicalServices findById(@PathVariable Long id);
}
