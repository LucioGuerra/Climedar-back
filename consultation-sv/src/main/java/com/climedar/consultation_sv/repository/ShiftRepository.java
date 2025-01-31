package com.climedar.consultation_sv.repository;

import com.climedar.consultation_sv.external.model.doctor.Shift;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@FeignClient(name = "doctor-sv")
public interface ShiftRepository {

    @GetMapping("/api/public/shifts/{id}")
    Shift findById(@PathVariable Long id);

    @GetMapping("/api/public/shifts")
    Shift getAllShift(@RequestParam Long doctorId, @RequestParam LocalDate date);

    @GetMapping("/api/public/shifts/ids")
    List<Shift> findAllById(@RequestParam Set<Long> ids);
}
