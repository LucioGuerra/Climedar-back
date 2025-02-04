package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.external.model.doctor.Shift;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@FeignClient(name = "doctor-sv")
public interface ShiftRepository {

    @GetMapping("/api/public/shifts/{id}")
    Shift findById(@PathVariable Long id);

    @GetMapping("/api/public/shifts")
    Page<Shift> getAllShift(@RequestParam Long doctorId, @RequestParam LocalDate date);

    @GetMapping("/api/public/shifts/ids")
    List<Shift> findAllById(@RequestParam Set<Long> ids);

    @PostMapping("/api/public/shifts/{id}/occupy")
    void occupyShift(@PathVariable Long id);

    @PostMapping("/api/public/shifts/{id}/clear")
    void clearShift(@PathVariable Long id);
}
