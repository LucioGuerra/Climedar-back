package com.climedar.medical_service_sv.controller;

import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.service.MedicalService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/services")
public class MedicalController {
    private final MedicalService medicalService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalServiceModel> getMedicalServiceById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(medicalService.getMedicalServiceById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkIfMedicalServiceExists(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(medicalService.checkIfMedicalServiceExists(id));
    }
}
