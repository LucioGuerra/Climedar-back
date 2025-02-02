package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServices;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Repository
@FeignClient(name = "medical-service-sv")
public interface MedicalServicesRepository {
    @GetMapping("/api/public/medical-services/{id}")
    MedicalServicesWrapped findById(@PathVariable Long id);

    @GetMapping("/api/public/medical-services/ids")
    List<MedicalServices> findAllById(@RequestParam Set<Long> ids);
}
