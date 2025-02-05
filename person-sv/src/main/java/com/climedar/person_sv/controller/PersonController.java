package com.climedar.person_sv.controller;

import com.climedar.person_sv.dto.request.create.CreatePersonDTO;
import com.climedar.person_sv.dto.request.update.UpdatePersonDTO;
import com.climedar.person_sv.dto.response.GetPersonDTO;
import com.climedar.person_sv.entity.Gender;
import com.climedar.person_sv.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/{id}")
    public ResponseEntity<GetPersonDTO> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<GetPersonDTO> getPersonByDni(@PathVariable String dni) {
        return personService.getPersonByDni(dni);
    }

    @GetMapping("/ids")
    public ResponseEntity<List<GetPersonDTO>> getPersonsByIds(@RequestParam Set<Long> ids) {
        return personService.getPersonsByIds(ids);
    }

    @GetMapping
    public ResponseEntity<Page<GetPersonDTO>> getAllPersons(
                                                        @PageableDefault(size = 10, sort = "surname", direction =
                                                                Sort.Direction.ASC) Pageable pageable,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String surname,
                                                        @RequestParam(required = false) String dni,
                                                        @RequestParam(required = false) Gender gender) {

        return personService.getAllPersons(pageable, name, surname, dni, gender);
    }

    @GetMapping("/fullName")
    public ResponseEntity<Page<GetPersonDTO>> getAllPersonsByFullName(
                                                        @PageableDefault(size = 10, sort = "surname", direction =
                                                                Sort.Direction.ASC) Pageable pageable,
                                                        @RequestParam(required = false) String fullName) {

        return personService.getPersonsByFullName(fullName, pageable);
    }

    @PostMapping
    public ResponseEntity<GetPersonDTO> createPerson(@RequestBody CreatePersonDTO createPersonDTO) {
        return personService.createPerson(createPersonDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetPersonDTO> updatePerson(@PathVariable Long id,
                                                      @RequestBody UpdatePersonDTO updatePersonDTO) {
        return personService.updatePerson(id, updatePersonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id);
    }

}
