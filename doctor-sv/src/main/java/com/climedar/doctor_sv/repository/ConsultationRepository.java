package com.climedar.doctor_sv.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "consultation-sv")
public interface ConsultationRepository {

    @GetMapping("/api/public/consultations")
    Boolean existConsultationForShift(@RequestParam Long shiftId);

}
