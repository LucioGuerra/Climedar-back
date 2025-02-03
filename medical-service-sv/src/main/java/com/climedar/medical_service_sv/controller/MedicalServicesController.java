package com.climedar.medical_service_sv.controller;

import com.climedar.medical_service_sv.dto.response.MedicalServicesWrapped;
import com.climedar.medical_service_sv.service.MedicalServicesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/medical-services")
public class MedicalServicesController {

    private final MedicalServicesService medicalServicesService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalServicesWrapped> getMedicalServicesById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(medicalServicesService.getMedicalServicesById(id));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<MedicalServicesWrapped>> getMedicalServicesByIds(@RequestParam Set<Long> ids) {
        return ResponseEntity.status(200).body(medicalServicesService.getMedicalServicesByIds(ids));
    }
}
