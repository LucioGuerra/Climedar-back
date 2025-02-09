package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "medical-service-sv")
public interface MedicalServicesRepository {
    @GetMapping("/api/public/medical-services/{id}")
    MedicalServicesWrapped findById(@PathVariable Long id);

    @GetMapping("/api/public/medical-services/ids")
    List<MedicalServicesWrapped> findAllById(@RequestParam Set<Long> ids);
}
