package com.climedar.doctor_sv.repository;


import com.climedar.doctor_sv.external.model.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(name = "person-sv")
public interface PersonRepository {

    @GetMapping("/api/public/persons/{id}")
    Person findById(@PathVariable Long id);

    @GetMapping("/api/public/persons/dni/{dni}")
    Optional<Person> findByDni(@PathVariable String dni);

    @PostMapping("/api/public/persons")
    Optional<Person> createPerson(Person person);

    @PatchMapping("/api/public/persons/{id}")
    void updatePerson(@PathVariable Long id, Person person);

}
