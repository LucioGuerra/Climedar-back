package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.external.model.doctor.Shift;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "doctor-sv") //todo: implementar fallback
public interface ShiftRepository {

    @GetMapping("/api/public/shifts/{id}")
    Shift findById(@PathVariable Long id);
}
