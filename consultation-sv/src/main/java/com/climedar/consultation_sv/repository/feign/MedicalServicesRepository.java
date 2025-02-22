package com.climedar.consultation_sv.repository.feign;

import com.climedar.consultation_sv.dto.request.MedicalServicesWrapped;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "medical-service-sv")
public interface MedicalServicesRepository {

    @GetMapping("/api/public/medical-services/ids")
    List<MedicalServicesWrapped> findAllById(@RequestParam Set<Long> ids);

    @GetMapping("/api/public/medical-services/codes")
    List<MedicalServicesWrapped> findAllByCode(@RequestParam Set<String> codes);
}
