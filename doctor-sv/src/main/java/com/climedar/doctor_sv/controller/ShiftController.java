package com.climedar.doctor_sv.controller;

import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/shifts")
public class ShiftController {
    private final ShiftService shiftService;

    @GetMapping("/{id}")
    public ResponseEntity<ShiftModel> getShiftById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(shiftService.getShiftById(id));
    }
}
