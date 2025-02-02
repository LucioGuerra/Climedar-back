package com.climedar.medical_service_sv.controller;

import com.climedar.medical_service_sv.dto.response.MedicalServicesWrapped;
import com.climedar.medical_service_sv.service.MedicalServicesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/medical-services")
public class MedicalServicesController {

    private final MedicalServicesService medicalServicesService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalServicesWrapped> getMedicalServicesById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(medicalServicesService.getMedicalServicesById(id));
    }
}
