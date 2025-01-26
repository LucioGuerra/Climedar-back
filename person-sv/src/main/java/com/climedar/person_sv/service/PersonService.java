package com.climedar.person_sv.service;

import com.climedar.person_sv.dto.request.CreatePersonDTO;
import com.climedar.person_sv.dto.request.UpdatePersonDTO;
import com.climedar.person_sv.dto.response.GetPersonDTO;
import com.climedar.person_sv.entity.Person;
import com.climedar.person_sv.mapper.PersonMapper;
import com.climedar.person_sv.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final AddressService addressService;

    public ResponseEntity<GetPersonDTO> getPersonById(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        return ResponseEntity.status(HttpStatus.OK).body(personMapper.toDTO(person));
    }

    public ResponseEntity<GetPersonDTO> createPerson(CreatePersonDTO createPersonDTO) {
        Person person = personMapper.toEntity(createPersonDTO);

        person.setAddress(addressService.asigneeAddress(createPersonDTO.address()));

        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personMapper.toDTO(person));
    }

    public ResponseEntity<GetPersonDTO> updatePerson(Long id, UpdatePersonDTO updatePersonDTO) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        personMapper.updateEntity(person, updatePersonDTO);

        if (updatePersonDTO.address() != null) {
            person.setAddress(addressService.asigneeAddress(updatePersonDTO.address()));
        }

        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.OK).body(personMapper.toDTO(person));
    }

    public ResponseEntity<Void> deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found with id: " + id));
        person.setDeleted(true);
        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
