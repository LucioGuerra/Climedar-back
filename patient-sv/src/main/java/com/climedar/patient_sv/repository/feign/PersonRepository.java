package com.climedar.patient_sv.repository.feign;


import com.climedar.patient_sv.external.model.Gender;
import com.climedar.patient_sv.external.model.Person;
import com.climedar.patient_sv.repository.feign.fallback.FallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/api/public/persons")
    Page<Person> getAllPersons(Pageable pageable,
                               @RequestParam(required = false) String fullName,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String surname,
                               @RequestParam(required = false) String dni,
                               @RequestParam(required = false) Gender gender);

    @PostMapping("/api/public/persons")
    Person createPerson(Person person);

    @PutMapping("/api/public/persons/{id}")
    Person updatePerson(@PathVariable Long id, @RequestBody Person person);

    @GetMapping("/api/public/persons/fullName")
    Page<Person> getAllPersonsByFullName(@PageableDefault(size = 10, sort = "surname", direction = Sort.Direction.ASC) Pageable pageable,
                                         @RequestParam(required = false) String fullName);

}
