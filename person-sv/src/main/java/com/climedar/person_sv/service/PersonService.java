package com.climedar.person_sv.service;

import com.climedar.person_sv.dto.request.create.CreatePersonDTO;
import com.climedar.person_sv.dto.request.update.UpdatePersonDTO;
import com.climedar.person_sv.dto.response.GetPersonDTO;
import com.climedar.person_sv.entity.Person;
import com.climedar.person_sv.entity.Gender;
import com.climedar.person_sv.mapper.PersonMapper;
import com.climedar.person_sv.repository.PersonRepository;
import com.climedar.person_sv.specification.PersonSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public ResponseEntity<List<GetPersonDTO>> getPersonsByIds(Set<Long> ids) {
        List<Person> persons = personRepository.findAllByIdAndNotDeleted(ids);
        List<GetPersonDTO> personsDTO = persons.stream().map(personMapper::toDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(personsDTO);
    }

    public ResponseEntity<Page<GetPersonDTO>> getAllPersons(Pageable pageable, String name,
                                                            String surname, String dni,
                                                            Gender gender) {

        Specification<Person> specification = Specification.where(PersonSpecification.deletedEqual(false))
                .and(PersonSpecification.nameLike(name))
                .and(PersonSpecification.surnameLike(surname))
                .and(PersonSpecification.dniLike(dni))
                .and(PersonSpecification.genderEqual(gender));

        Page<Person> persons = personRepository.findAll(specification, pageable);
        Page<GetPersonDTO> personsDTO = persons.map(personMapper::toDTO);
        return ResponseEntity.status(HttpStatus.OK).body(personsDTO);
    }
}
