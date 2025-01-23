package com.climedar.medical_service_sv.service;

import com.climedar.library.exception.ClimedarException;
import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalPackageMapper;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.repository.MedicalPackageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PackageService {

    private final MedicalPackageRepository medicalPackageRepository;
    private final MedicalPackageMapper medicalPackageMapper;
    private final MedicalService medicalService;

    public MedicalPackageModel getPackageById(Long id) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }

    public List<MedicalPackageModel> getAllPackages() {
        List<MedicalPackageEntity> medicalPackageEntities = medicalPackageRepository.findAll();
        return medicalPackageEntities.stream().map(medicalPackageMapper::toModel).toList();
    }

    public MedicalPackageModel createPackage(MedicalPackageModel medicalPackageModel) { //todo: modificar esto
        MedicalPackageEntity medicalPackageEntity = medicalPackageMapper.toEntity(medicalPackageModel);
        medicalPackageEntity = medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }


    public void deletePackage(Long id) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Package not found with id: " + id));
        medicalPackageEntity.setDeleted(true);
        medicalPackageRepository.save(medicalPackageEntity);
    }

    public MedicalPackageModel addServiceToPackage(Long packageId, Long serviceId) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(packageId).orElseThrow(() -> new EntityNotFoundException("Package not found with id: " + packageId));
        MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);

        if (medicalPackageEntity.getServices().contains(medicalServiceEntity)) {
            throw new ClimedarException("SERVICE_ALREADY_EXIST", "Service already exists in package");
        }

        medicalPackageEntity.addService(medicalServiceEntity);
        medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }

    public MedicalPackageModel removeServiceFromPackage(Long packageId, Long serviceId) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(packageId).orElseThrow(() -> new RuntimeException("Package not found with id: " + packageId));
        MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);

        if (!medicalPackageEntity.getServices().contains(medicalServiceEntity)) {
            throw new ClimedarException("SERVICE_NOT_EXIST", "Service does not exist in package");
        }

        medicalPackageEntity.removeService(medicalServiceEntity);
        medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }
}
