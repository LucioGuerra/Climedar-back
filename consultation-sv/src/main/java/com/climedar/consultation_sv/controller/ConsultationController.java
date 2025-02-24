package com.climedar.consultation_sv.controller;

import com.climedar.consultation_sv.adapter.ConsultationRestAdapter;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationRestAdapter consultationRestAdapter;

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationModel> getConsultation(@PathVariable Long id){
        return ResponseEntity.ok(consultationRestAdapter.getConsultationById(id));
    }

    @GetMapping("/ids")
    public ResponseEntity<List<ConsultationModel>> getAllConsultationsByIds(@RequestParam("consultationIds") Set<Long> consultationIds){
        return ResponseEntity.ok(consultationRestAdapter.getConsultationsByIds(consultationIds));
    }

}
