package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.entity.MedicalServices;
import com.climedar.medical_service_sv.repository.MedicalServicesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class MedicalServicesService {

    private final MedicalServicesRepository medicalServicesRepository;

    public MedicalServices getMedicalServicesById(Long id) {
        return medicalServicesRepository.findMedicalServicesById(id);
    }
}
