package com.climedar.consultation_sv.service;

import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.repository.ConsultationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public ConsultationModel getConsultationById(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }
}
