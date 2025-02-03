package com.climedar.doctor_sv.controller;

import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping("/{id}")
    public ResponseEntity<ShiftModel> getShiftById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(shiftService.getShiftById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ShiftModel>> getAllShifts(@RequestParam(required = false) Long doctorId,
                                                         @RequestParam(required = false) LocalDate date,
                                                         @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.status(200).body(shiftService.getAllShifts(pageable, new ShiftSpecificationDTO(doctorId, date)));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<ShiftModel>> findAllById(@RequestParam Set<Long> ids) {
        return ResponseEntity.status(200).body(shiftService.findAllById(ids));
    }
}
