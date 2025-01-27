package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.model.SpecialityModel;
import com.climedar.doctor_sv.repository.SpecialityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SpecialityService {

    private final SpecialityRepository specialityRepository;

    public Speciality findById(Long id) {
        return specialityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Speciality not found with id: " + id));
    }
}
