package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.external.model.medical_services.MedicalServicesWrapped;
import com.climedar.payment_sv.external.model.medical_services.ServiceType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "medical-service-sv")
public interface MedicalServicesRepository {
    @GetMapping("/api/public/medical-services/{id}")
    MedicalServicesWrapped getMedicalServiceById(@PathVariable Long id);

    @GetMapping("/api/public/medical-services/ids")
    List<MedicalServicesWrapped> getMedicalServicesByIds(@RequestParam List<Long> ids);

    @GetMapping("/api/public/medical-services/types")
    Set<ServiceType> getAllServicesType();
}
