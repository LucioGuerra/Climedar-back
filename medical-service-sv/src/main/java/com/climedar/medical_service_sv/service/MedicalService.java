package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalServiceMapper;
import com.climedar.medical_service_sv.repository.MedicalServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MedicalService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;

    public MedicalServiceModel getMedicalServiceById(Long id) {
       MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));

       return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public Page<MedicalServiceModel> getAllMedicalServices(Pageable pageable) {
        Specification<MedicalServiceEntity> specification = Specification.where((root, query, cb) -> cb.equal(root.get("deleted"), false));

        Page<MedicalServiceEntity> medicalServiceEntities = medicalServiceRepository.findAll(specification, pageable);

        return medicalServiceEntities.map(medicalServiceMapper::toModel);
    }

    public MedicalServiceModel createMedicalService(MedicalServiceModel medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceMapper.toEntity(medicalServiceModel);

        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public MedicalServiceModel updateMedicalService(Long id, MedicalServiceModel medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));

        updateEntityFields(medicalServiceModel, medicalServiceEntity);

        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public void deleteMedicalService(Long id) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
        medicalServiceEntity.setDeleted(true);
        medicalServiceRepository.save(medicalServiceEntity);
    }

    private void updateEntityFields(MedicalServiceModel medicalServiceModel, MedicalServiceEntity medicalServiceEntity) {
        Optional.ofNullable(medicalServiceModel.getName())
                .ifPresent(medicalServiceEntity::setName);
        Optional.ofNullable(medicalServiceModel.getPrice())
                .ifPresent(medicalServiceEntity::setPrice);
        Optional.ofNullable(medicalServiceModel.getServiceType())
                .ifPresent(medicalServiceEntity::setServiceType);
        Optional.ofNullable(medicalServiceModel.getDescription())
                .ifPresent(medicalServiceEntity::setDescription);
    }

    public MedicalServiceEntity getMedicalServiceEntityById(Long id) {
        return medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
    }
}
