package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.external.model.Consultation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "consultation-sv")
public interface ConsultationRepository {

    @GetMapping("/api/public/consultations/{id}")
    Consultation getConsultation(@PathVariable Long id);
}
