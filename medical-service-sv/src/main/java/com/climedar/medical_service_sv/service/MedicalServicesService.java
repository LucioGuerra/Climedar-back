package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.dto.response.MedicalServicesWrapped;
import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalPackageMapper;
import com.climedar.medical_service_sv.mapper.MedicalServiceMapper;
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

    public MedicalServicesWrapped getMedicalServicesById(Long id) {
        MedicalServicesWrapped medicalServicesWrapped = new MedicalServicesWrapped();

        Optional<MedicalPackageEntity> medicalPackageEntity = medicalPackageRepository.findByIdAndNotDeleted(id);
        medicalPackageEntity.ifPresent(entity -> medicalServicesWrapped.setMedicalPackage(medicalPackageMapper.toModel(entity)));

        Optional<MedicalServiceEntity> medicalService = medicalServiceRepository.findByIdAndNotDeleted(id);
        medicalService.ifPresent(medicalServiceEntity -> medicalServicesWrapped.setMedicalService(medicalServiceMapper.toModel(medicalServiceEntity)));

        if (medicalServicesWrapped.getMedicalPackage() == null && medicalServicesWrapped.getMedicalService() == null) {
            throw new EntityNotFoundException("Medical services not found with id: " + id);
        }
        return medicalServicesWrapped;
    }
}
