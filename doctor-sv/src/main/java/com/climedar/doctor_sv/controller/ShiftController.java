package com.climedar.doctor_sv.controller;

import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.dto.request.ShiftBuilder;
import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping("/{id}")
    public ResponseEntity<ShiftModel> getShiftById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(shiftService.getShiftById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ShiftModel>> getAllShifts(@RequestParam(required = false) String date,
                                                         @RequestParam(required = false) String fromTime,
                                                         @RequestParam(required = false) String toTime,
                                                         @RequestParam(required = false) String starTime,
                                                         @PageableDefault(size = 1000) Pageable pageable) {
        return ResponseEntity.status(200).body(shiftService.getAllShifts(pageable, new ShiftSpecificationDTO(date, fromTime, toTime, starTime)));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<ShiftModel>> findAllById(@RequestParam Set<Long> ids) {
        return ResponseEntity.status(200).body(shiftService.findAllById(ids));
    }

    @GetMapping("/date/{date}/occupy")
    public ResponseEntity<List<ShiftModel>> getShiftsByDateAndOccupied(@PathVariable String date) {
        return ResponseEntity.status(200).body(shiftService.getShiftsByDateAndOccupied(LocalDate.parse(date)));
    }

    @PostMapping
    public ResponseEntity<ShiftModel> createShift(@RequestParam Long doctorId, @RequestParam Duration timeOfShifts) {
        return ResponseEntity.status(201).body(shiftService.createShift(new CreateShiftDTO(doctorId, timeOfShifts,
                ShiftBuilder.OVERTIME)).get(0));
    }

    @PostMapping("/{id}/occupy")
    public ResponseEntity<Void> occupyShift(@PathVariable Long id) {
        shiftService.occupyShift(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/clear")
    public ResponseEntity<Void> clearShift(@PathVariable Long id) {
        shiftService.clearShift(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
