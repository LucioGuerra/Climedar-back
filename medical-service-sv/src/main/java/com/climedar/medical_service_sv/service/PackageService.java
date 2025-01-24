package com.climedar.medical_service_sv.service;

import com.climedar.library.exception.ClimedarException;
import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.response.MedicalPackagePage;
import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalPackageMapper;
import com.climedar.medical_service_sv.mapper.PageInfoMapper;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.repository.MedicalPackageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<MedicalPackageModel> getAllPackages(Pageable pageable) {
        Specification<MedicalPackageEntity> specification = Specification.where((root, query, cb) -> cb.equal(root.get("deleted"), false));
        Page<MedicalPackageEntity> medicalPackageEntities = medicalPackageRepository.findAll(specification, pageable);
        return medicalPackageEntities.map(medicalPackageMapper::toModel);

    }

    public MedicalPackageModel createPackage(List<Long> serviceIds) {
        MedicalPackageEntity medicalPackageEntity = new MedicalPackageEntity();
        for (Long serviceId : serviceIds) {
            MedicalServiceEntity medicalServiceEntity = medicalService.getMedicalServiceEntityById(serviceId);
            medicalPackageEntity.addService(medicalServiceEntity);
        }
        medicalPackageEntity.setCode(generateCode());
        medicalPackageEntity = medicalPackageRepository.save(medicalPackageEntity);
        return medicalPackageMapper.toModel(medicalPackageEntity);
    }


    public Boolean deletePackage(Long id) {
        MedicalPackageEntity medicalPackageEntity = medicalPackageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Package not found with id: " + id));
        medicalPackageEntity.setDeleted(true);
        medicalPackageRepository.save(medicalPackageEntity);
        return true;
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

    private String generateCode() {
        return "CODE";
    }

    public Boolean checkIfPackageExists(Long id) {
        Optional<MedicalPackageEntity> medicalPackageEntity = medicalPackageRepository.findByIdAndNotDeleted(id);
        return medicalPackageEntity.isPresent();
    }
}
