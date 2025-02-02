package com.climedar.medical_service_sv.controller;

import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.service.PackageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/packages")
public class PackageController {

    private final PackageService packageService;

    @GetMapping("/{id}")
    public ResponseEntity<MedicalPackageModel> getPackageById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(packageService.getPackageById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkIfPackageExists(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(packageService.checkIfPackageExists(id));
    }
}
