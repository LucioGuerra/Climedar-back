package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.dto.request.CreateMedicalDTO;
import com.climedar.medical_service_sv.dto.request.UpdateMedicalServiceDTO;
import com.climedar.medical_service_sv.entity.ServiceType;
import com.climedar.medical_service_sv.external.model.Speciality;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalServiceMapper;
import com.climedar.medical_service_sv.repository.MedicalServiceRepository;
import com.climedar.medical_service_sv.repository.SpecialityRepository;
import com.climedar.medical_service_sv.specification.MedicalSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MedicalService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;
    private final SpecialityRepository specialityRepository;
    private final ApplicationEventPublisher eventPublisher;

    public MedicalServiceModel getMedicalServiceById(Long id) {
       MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
       return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public Page<MedicalServiceModel> getAllMedicalServices(Pageable pageable, String name, String code,
                                                           String description, ServiceType serviceType, Long specialityId) {

        Specification<MedicalServiceEntity> specification = Specification.where(MedicalSpecification.deletedEqual(false))
                .and(MedicalSpecification.codeContains(code))
                .and(MedicalSpecification.nameContains(name))
                .and(MedicalSpecification.descriptionContains(description))
                .and(MedicalSpecification.serviceTypeEqual(serviceType))
                .and(MedicalSpecification.specialityIdEqual(specialityId));

        Page<MedicalServiceEntity> medicalServiceEntities = medicalServiceRepository.findAll(specification, pageable);

        return medicalServiceEntities.map(medicalServiceMapper::toModel);
    }

    public MedicalServiceModel createMedicalService(CreateMedicalDTO medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceMapper.toEntity(medicalServiceModel);
        Speciality speciality = specialityRepository.getSpecialityById(medicalServiceModel.specialityId());
        medicalServiceEntity.setCode(generateCode(medicalServiceEntity.getServiceType().toString(), speciality.getName()));
        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public MedicalServiceModel updateMedicalService(Long id, UpdateMedicalServiceDTO medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));

        updateEntityFields(medicalServiceModel, medicalServiceEntity);

        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public Boolean deleteMedicalService(Long id) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
        medicalServiceEntity.setDeleted(true);
        eventPublisher.publishEvent(medicalServiceEntity);
        medicalServiceRepository.save(medicalServiceEntity);
        return true;
    }

    private void updateEntityFields(UpdateMedicalServiceDTO medicalServiceModel, MedicalServiceEntity medicalServiceEntity) {
        Optional.ofNullable(medicalServiceModel.name())
                .ifPresent(medicalServiceEntity::setName);
        Optional.ofNullable(medicalServiceModel.price())
                .ifPresent(medicalServiceEntity::setPrice);
        Optional.ofNullable(medicalServiceModel.description())
                .ifPresent(medicalServiceEntity::setDescription);
        Optional.ofNullable(medicalServiceModel.estimatedDuration())
                .ifPresent(duration -> medicalServiceEntity.setEstimatedDuration(Duration.parse(duration)));
    }

    public MedicalServiceEntity getMedicalServiceEntityById(Long id) {
        return medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
    }


    private String generateCode(String serviceType, String serviceSpeciality) {
        String type;
        if(serviceType.length() >= 4){
            type = serviceType.substring(0, 4);
        }else{
            type = String.format("%-4s", serviceType).replace(' ', 'X');
        }

        String speciality;
        serviceSpeciality = serviceSpeciality.toUpperCase();
        if(serviceType.length() >= 3){
            speciality = serviceSpeciality.substring(0, 3);
        }else{
            speciality = String.format("%-3s", serviceSpeciality).replace(' ', 'X');
        }

        Long count = medicalServiceRepository.count();
        String number = String.format("%05d", count);
        return String.format("MS-%s-%s-%s", type, speciality, number);
    }

    public Boolean checkIfMedicalServiceExists(Long id) {
        Optional<MedicalServiceEntity> medicalServiceEntity = medicalServiceRepository.findByIdAndNotDeleted(id);
        return medicalServiceEntity.isPresent();
    }

    public MedicalServiceModel getMedicalServiceByCode(String code) {
        MedicalServiceEntity entity = medicalServiceRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Medical service not found with code: " + code));
        return medicalServiceMapper.toModel(entity);
    }
}
