package com.climedar.doctor_sv.controller;

import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.model.SpecialityModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.climedar.doctor_sv.service.SpecialityService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/specialities")
public class SpecialityController {

    private final SpecialityService specialityService;


    @GetMapping("/{id}")
    public SpecialityModel getSpecialityById(@PathVariable Long id) {
        return specialityService.getSpecialityById(id);
    }
}
