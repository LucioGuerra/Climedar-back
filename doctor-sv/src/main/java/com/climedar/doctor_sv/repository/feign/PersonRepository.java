package com.climedar.doctor_sv.repository.feign;


import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.repository.feign.fallback.FallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@FeignClient(name = "person-sv", fallbackFactory = FallBackFactory.class)
public interface PersonRepository {

    @GetMapping("/api/public/persons/{id}")
    Person findById(@PathVariable Long id);

    @GetMapping("/api/public/persons/ids")
    List<Person> findAllById(@RequestParam Set<Long> ids);

    @GetMapping("/api/public/persons/dni/{dni}")
    Optional<Person> findByDni(@PathVariable String dni);

    @PostMapping("/api/public/persons")
    Person createPerson(Person person);

    @PatchMapping("/api/public/persons/{id}")
    Person updatePerson(@PathVariable Long id, Person person);

}
