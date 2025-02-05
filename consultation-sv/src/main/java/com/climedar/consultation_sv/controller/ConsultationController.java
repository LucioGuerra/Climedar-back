package com.climedar.consultation_sv.controller;

import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationModel> getConsultation(@PathVariable Long id){
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

}
