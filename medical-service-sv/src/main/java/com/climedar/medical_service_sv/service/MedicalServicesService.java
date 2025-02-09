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

import java.util.*;

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
        medicalPackageEntity.ifPresent(entity -> medicalServicesWrapped.setMedicalPackageModel(medicalPackageMapper.toModel(entity)));

        Optional<MedicalServiceEntity> medicalService = medicalServiceRepository.findByIdAndNotDeleted(id);
        medicalService.ifPresent(medicalServiceEntity -> medicalServicesWrapped.setMedicalServiceModel(medicalServiceMapper.toModel(medicalServiceEntity)));

        if (medicalServicesWrapped.getMedicalPackageModel() == null && medicalServicesWrapped.getMedicalServiceModel() == null) {
            throw new EntityNotFoundException("Medical services not found with id: " + id);
        }
        return medicalServicesWrapped;
    }

    public List<MedicalServicesWrapped> getMedicalServicesByIds(Set<Long> ids) {
        List<MedicalPackageEntity> packageEntities = medicalPackageRepository.findByIdInAndNotDeleted(ids);
        List<MedicalServiceEntity> serviceEntities = medicalServiceRepository.findByIdInAndNotDeleted(ids);

        Map<Long, MedicalServicesWrapped> wrappedMap = new HashMap<>();

        for (MedicalPackageEntity packageEntity : packageEntities) {
            MedicalServicesWrapped wrapped = new MedicalServicesWrapped();
            wrapped.setMedicalPackageModel(medicalPackageMapper.toModel(packageEntity));
            wrappedMap.put(packageEntity.getId(), wrapped);
        }

        for (MedicalServiceEntity serviceEntity : serviceEntities) {
            MedicalServicesWrapped wrapped = new MedicalServicesWrapped();
            wrapped.setMedicalServiceModel(medicalServiceMapper.toModel(serviceEntity));
            wrappedMap.put(serviceEntity.getId(), wrapped);
        }

        if (wrappedMap.isEmpty()) {
            throw new EntityNotFoundException("No medical services found for the provided IDs: " + ids);
        }

        return new ArrayList<>(wrappedMap.values());
    }
}
