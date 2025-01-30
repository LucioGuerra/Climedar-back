package com.climedar.consultation_sv.controller;

import com.climedar.consultation_sv.service.ConsultationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/consultations")
public class ConsultationController {

    private final ConsultationService consultationService;

}
