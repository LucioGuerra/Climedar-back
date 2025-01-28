package com.climedar.person_sv.service;

import com.climedar.person_sv.dto.request.create.CreatePersonDTO;
import com.climedar.person_sv.dto.request.update.UpdatePersonDTO;
import com.climedar.person_sv.dto.response.GetPersonDTO;
import com.climedar.person_sv.entity.Person;
import com.climedar.person_sv.mapper.PersonMapper;
import com.climedar.person_sv.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final AddressService addressService;

    public ResponseEntity<GetPersonDTO> getPersonById(Long id) {
        Person person = personRepository.findByIdAndNotDeleted(id).orElseThrow(() -> new EntityNotFoundException(
                "Person not found with id: " + id));
        return ResponseEntity.status(HttpStatus.OK).body(personMapper.toDTO(person));
    }

    public ResponseEntity<GetPersonDTO> createPerson(CreatePersonDTO createPersonDTO) {
        Person person = personMapper.toEntity(createPersonDTO);

        person.setAddress(addressService.createAddress(createPersonDTO.address()));

        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personMapper.toDTO(person));
    }

    public ResponseEntity<GetPersonDTO> updatePerson(Long id, UpdatePersonDTO updatePersonDTO) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found with personId: " + id));
        personMapper.updateEntity(person, updatePersonDTO);

        if (updatePersonDTO.address() != null) {
            person.setAddress(addressService.createAddress(updatePersonDTO.address()));
        }

        //todo: Si se cambia el dni se deberia crear otra persona

        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.OK).body(personMapper.toDTO(person));
    }

    public ResponseEntity<Void> deletePerson(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Person not found with personId: " + id));
        person.setDeleted(true);
        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<GetPersonDTO> getPersonByDni(String dni) {
        Person person = personRepository.findByDni(dni).orElseThrow(() -> new EntityNotFoundException("Person not found with dni: " + dni));
        return ResponseEntity.status(HttpStatus.OK).body(personMapper.toDTO(person));
    }
}
