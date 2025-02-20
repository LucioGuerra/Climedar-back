package com.climedar.payment_sv.repository;

import com.climedar.payment_sv.external.model.Consultation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "consultation-sv")
public interface ConsultationRepository {

    @GetMapping("/api/public/consultations/{id}")
    Consultation getConsultation(@PathVariable Long id);

    @GetMapping("/api/public/consultations/ids")
    List<Consultation> findAllById(@RequestParam List<Long> consultationIds);
}
