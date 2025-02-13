package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.mapper.SpecialityMapper;
import com.climedar.doctor_sv.model.SpecialityModel;
import com.climedar.doctor_sv.repository.SpecialityRepository;
import com.climedar.doctor_sv.specification.SpecialitySpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class SpecialityService {

    private final SpecialityRepository specialityRepository;
    private final SpecialityMapper specialityMapper;

    public Speciality findEntityById(Long id) {
        return specialityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Speciality not found with id: " + id));
    }

    public SpecialityModel getSpecialityById(Long id) {
        Speciality speciality = this.findEntityById(id);
        return specialityMapper.toModel(speciality);
    }

    public Page<SpecialityModel> getAllSpecialities(Pageable pageable, String name,
                                                    String description, String code) {

        Specification<Speciality> specification = Specification.where(SpecialitySpecification.deletedEqual(false))
                .and(SpecialitySpecification.nameLike(name))
                .and(SpecialitySpecification.descriptionLike(description))
                .and(SpecialitySpecification.codeLike(code));

        Page<Speciality> specialities = specialityRepository.findAll(specification, pageable);

        return specialities.map(specialityMapper::toModel);
    }

    public SpecialityModel createSpeciality(SpecialityModel specialityModel) {
        Speciality speciality = specialityMapper.toEntity(specialityModel);
        specialityRepository.save(speciality);
        return specialityMapper.toModel(speciality);
    }

    public SpecialityModel updateSpeciality(Long id, SpecialityModel specialityModel) {
        Speciality specialityToUpdate = this.findEntityById(id);
        specialityMapper.updateEntity(specialityToUpdate, specialityModel);
        specialityRepository.save(specialityToUpdate);
        return specialityMapper.toModel(specialityToUpdate);
    }

    public Boolean deleteSpeciality(Long id) {
        Speciality specialityToDelete = findEntityById(id);
        specialityToDelete.setDeleted(true);
        specialityRepository.save(specialityToDelete);
        return true;
    }


    public Set<String> getAllSpecialitiesNames() {
        return specialityRepository.getAllSpecialitiesNames();
    }
}
