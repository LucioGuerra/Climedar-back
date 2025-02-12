package com.climedar.patient_sv.service;

import com.climedar.patient_sv.entity.MedicalSecure;
import com.climedar.patient_sv.mapper.MedicalSecureMapper;
import com.climedar.patient_sv.model.MedicalSecureModel;
import com.climedar.patient_sv.repository.MedicalSecureRepository;
import com.climedar.patient_sv.specification.MedicalSecureSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class MedicalSecureService {

    private final MedicalSecureRepository medicalSecureRepository;
    private final MedicalSecureMapper medicalSecureMapper;

    public MedicalSecure findEntityById(Long id) {
        return medicalSecureRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("MedicalSecure not found with id: " + id));
    }

    public MedicalSecureModel getMedicalSecureById(Long id) {
        MedicalSecure medicalSecure = this.findEntityById(id);
        return medicalSecureMapper.toModel(medicalSecure);
    }

    public Page<MedicalSecureModel> getAllMedicalSecures(Pageable pageable, String name) {

        Specification<MedicalSecure> specification = Specification.where(MedicalSecureSpecification.deletedEqual(false))
                .and(MedicalSecureSpecification.nameLike(name));

        Page<MedicalSecure> specialities = medicalSecureRepository.findAll(specification, pageable);

        return specialities.map(medicalSecureMapper::toModel);
    }

    public MedicalSecureModel createMedicalSecure(MedicalSecureModel medicalSecureModel) {
        MedicalSecure medicalSecure = medicalSecureMapper.toEntity(medicalSecureModel);
        medicalSecureRepository.save(medicalSecure);
        return medicalSecureMapper.toModel(medicalSecure);
    }

    public MedicalSecureModel updateMedicalSecure(Long id, MedicalSecureModel medicalSecureModel) {
        MedicalSecure medicalSecureToUpdate = this.findEntityById(id);
        medicalSecureMapper.updateEntity(medicalSecureToUpdate, medicalSecureModel);
        medicalSecureRepository.save(medicalSecureToUpdate);
        return medicalSecureMapper.toModel(medicalSecureToUpdate);
    }

    public Boolean deleteMedicalSecure(Long id) {
        MedicalSecure medicalSecureToDelete = findEntityById(id);
        medicalSecureToDelete.setDeleted(true);
        medicalSecureRepository.save(medicalSecureToDelete);
        return true;
    }

}
