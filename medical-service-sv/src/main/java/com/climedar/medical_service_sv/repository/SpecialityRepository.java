package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.external.model.Speciality;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
@FeignClient(name = "speciality-sv")
public interface SpecialityRepository {

    @GetMapping("/api/pubic/specialities/{id}")
    Speciality getSpecialityById(@PathVariable Long id);
}
