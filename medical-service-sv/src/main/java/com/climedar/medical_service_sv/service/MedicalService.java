package com.climedar.medical_service_sv.service;

import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.response.MedicalServicePage;
import com.climedar.medical_service_sv.entity.ServiceType;
import com.climedar.medical_service_sv.mapper.PageInfoMapper;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.mapper.MedicalServiceMapper;
import com.climedar.medical_service_sv.repository.MedicalServiceRepository;
import com.climedar.medical_service_sv.specification.MedicalSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class MedicalService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;

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

    public MedicalServiceModel createMedicalService(MedicalServiceModel medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceMapper.toEntity(medicalServiceModel);
        //todo: Realizar pegada a doctor-sv para obtener la especialidad por el id
        medicalServiceEntity.setCode(generateCode(medicalServiceEntity.getServiceType().toString(), "Especialidad"));
        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public MedicalServiceModel updateMedicalService(Long id, MedicalServiceModel medicalServiceModel) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));

        updateEntityFields(medicalServiceModel, medicalServiceEntity);

        medicalServiceEntity = medicalServiceRepository.save(medicalServiceEntity);

        return medicalServiceMapper.toModel(medicalServiceEntity);
    }

    public Boolean deleteMedicalService(Long id) {
        MedicalServiceEntity medicalServiceEntity = medicalServiceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Medical service not found with id: " + id));
        medicalServiceEntity.setDeleted(true);
        medicalServiceRepository.save(medicalServiceEntity);
        return true;
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


    private String generateCode(String serviceType, String serviceSpeciality) {
        String type;
        if(serviceType.length() >= 4){
            type = serviceType.substring(0, 4);
        }else{
            type = String.format("%-4s", serviceType).replace(' ', 'X');
        }

        String area;
        if(serviceType.length() >= 3){
            area = serviceSpeciality.substring(0, 3);
        }else{
            area = String.format("%-3s", serviceSpeciality).replace(' ', 'X');
        }

        Long count = medicalServiceRepository.count();
        String number = String.format("%05d", count);
        return String.format("MS-%s-%s-%s", type, area, number);
    }

    public Boolean checkIfMedicalServiceExists(Long id) {
        Optional<MedicalServiceEntity> medicalServiceEntity = medicalServiceRepository.findByIdAndNotDeleted(id);
        return medicalServiceEntity.isPresent();
    }
}
