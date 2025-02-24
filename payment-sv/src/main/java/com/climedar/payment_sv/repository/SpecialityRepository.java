package com.climedar.payment_sv.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@FeignClient(name = "doctor-sv")
public interface SpecialityRepository {

    @GetMapping("/api/specialities/names")
    Set<String> getAllSpecialitiesName();
}
