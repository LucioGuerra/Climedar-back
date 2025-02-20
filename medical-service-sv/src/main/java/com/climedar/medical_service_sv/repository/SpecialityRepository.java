package com.climedar.medical_service_sv.repository;

import com.climedar.medical_service_sv.external.model.Speciality;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;


@FeignClient(name = "doctor-sv")
public interface SpecialityRepository {

    @GetMapping("/api/public/specialities/{id}")
    Speciality getSpecialityById(@PathVariable Long id);

    @GetMapping("/api/public/specialities/ids")
    Set<Speciality> getAllSpecialitiesByIds(@RequestParam Set<Long> ids);
}
