package com.climedar.patient_sv.controller;

import com.climedar.patient_sv.model.PatientModel;
import com.climedar.patient_sv.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<PatientModel> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<PatientModel>> getAllPatientsByIds(@RequestParam Set<Long> ids){
        return ResponseEntity.ok(patientService.getAllPatientsByIds(ids));
    }
}
