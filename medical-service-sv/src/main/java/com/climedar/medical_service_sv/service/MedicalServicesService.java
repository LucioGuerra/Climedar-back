package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.entity.MedicalServicesEntity;
import com.climedar.medical_service_sv.mapper.MedicalPackageMapper;
import com.climedar.medical_service_sv.mapper.MedicalServiceMapper;
import com.climedar.medical_service_sv.model.MedicalServices;
import com.climedar.medical_service_sv.repository.MedicalPackageRepository;
import com.climedar.medical_service_sv.repository.MedicalServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MedicalServicesService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalPackageRepository medicalPackageRepository;
    private final MedicalServiceMapper medicalServiceMapper;
    private final MedicalPackageMapper medicalPackageMapper;

    public MedicalServices getMedicalServicesById(Long id) {
        Optional<MedicalPackageEntity> medicalPackageEntity = medicalPackageRepository.findByIdAndNotDeleted(id);
        if (medicalPackageEntity.isPresent()) {
            return medicalPackageMapper.toModel(medicalPackageEntity.get());
        }
        Optional<MedicalServiceEntity> medicalService = medicalServiceRepository.findByIdAndNotDeleted(id);
        if (medicalService.isPresent()) {
            return medicalServiceMapper.toModel(medicalService.get());
        }
        throw new EntityNotFoundException("Medical services not found with id: " + id);
    }
}
