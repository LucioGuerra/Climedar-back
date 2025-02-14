package com.climedar.doctor_sv.controller;

import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.model.SpecialityModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.climedar.doctor_sv.service.SpecialityService;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/specialities")
public class SpecialityController {

    private final SpecialityService specialityService;


    @GetMapping("/{id}")
    public ResponseEntity<SpecialityModel> getSpecialityById(@PathVariable Long id) {
        return ResponseEntity.ok().body(specialityService.getSpecialityById(id));
    }

    @GetMapping("/names")
    public ResponseEntity<Set<String>> getAllSpecialitiesNames() {
        return ResponseEntity.ok().body(specialityService.getAllSpecialitiesNames());
    }
}
