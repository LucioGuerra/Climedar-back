package com.climedar.person_sv.controller;

import com.climedar.person_sv.dto.request.create.CreatePersonDTO;
import com.climedar.person_sv.dto.request.update.UpdatePersonDTO;
import com.climedar.person_sv.dto.response.GetPersonDTO;
import com.climedar.person_sv.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/public/person")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/{id}")
    public ResponseEntity<GetPersonDTO> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @PostMapping
    public ResponseEntity<GetPersonDTO> createPerson(@RequestBody CreatePersonDTO createPersonDTO) {
        return personService.createPerson(createPersonDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GetPersonDTO> updatePerson(@PathVariable Long id, @RequestBody UpdatePersonDTO updatePersonDTO) {
        return personService.updatePerson(id, updatePersonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id);
    }

}
