package com.climedar.consultation_sv.repository.feign;

import com.climedar.consultation_sv.external.model.doctor.Doctor;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@FeignClient(name = "doctor-sv")
public interface ShiftRepository {

    @GetMapping("/api/shifts/{id}")
    Optional<Shift> findById(@PathVariable Long id);

    @GetMapping("/api/shifts")
    Page<Shift> getAllShift(@RequestParam LocalDate date, @RequestParam LocalTime starTime, @RequestParam LocalTime fromTime, @RequestParam LocalTime toTime);

    @GetMapping("/api/shifts/date/{date}/occupy")
    List<Shift> getShiftsByDateAndOccupied(@PathVariable String date);

    @GetMapping("/api/shifts/ids")
    List<Shift> findAllById(@RequestParam Set<Long> ids);

    @PostMapping("/api/shifts/{id}/occupy")
    void occupyShift(@PathVariable Long id);

    @PostMapping("/api/shifts/{id}/clear")
    void clearShift(@PathVariable Long id);

    @PostMapping("/api/shifts")
    Shift createShift(@RequestParam Long doctorId, @RequestParam Duration timeOfShifts);

    @GetMapping("/api/doctors/{id}")
    Doctor findDoctorById(@PathVariable Long id);
}
