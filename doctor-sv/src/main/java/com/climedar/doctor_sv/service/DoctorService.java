package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.external.model.Gender;
import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.mapper.DoctorMapper;
import com.climedar.doctor_sv.mapper.PersonMapper;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.repository.DoctorRepository;
import com.climedar.doctor_sv.repository.feign.PersonRepository;
import com.climedar.doctor_sv.specification.DoctorSpecification;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PersonRepository personRepository;
    private final SpecialityService specialityService;
    private final PersonMapper personMapper;


    public DoctorModel getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found" +
                " with id: " + id));
        return doctorMapper.toModel(doctor, personRepository.findById(doctor.getPersonId()));
    }

    public Page<DoctorModel> getAllDoctors(Pageable pageable, String name,
                                    String surname, String dni,
                                    Gender gender, Long shiftId,
                                    Long specialtyId) {

        Specification<Doctor> specification = Specification.where(DoctorSpecification.deletedEqual(false))
                .and(DoctorSpecification.nameLike(name))
                .and(DoctorSpecification.surnameLike(surname))
                .and(DoctorSpecification.dniLike(dni))
                .and(DoctorSpecification.genderEqual(gender)
                .and(DoctorSpecification.shiftIdEqual(shiftId)
                .and(DoctorSpecification.specialtyIdEqual(specialtyId))));

        Page<Doctor> doctors = doctorRepository.findAll(specification, pageable);


        Set<Long> personIds = doctors.stream()
                .map(Doctor::getPersonId)
                .collect(Collectors.toSet());

        Map<Long, Person> personMap = personIds.isEmpty()
                ? Collections.emptyMap()
                : personRepository.findAllById(personIds).stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));


        return doctors.map(doctor -> {
            Person person = personMap.get(doctor.getPersonId());
            return doctorMapper.toModel(doctor, person);
        });
    }

    @Transactional
    public DoctorModel createDoctor(DoctorModel doctorModel) {
        Optional<Person> personOptional = personRepository.findByDni(doctorModel.getDni());
        Person person;
        if (personOptional.isEmpty()) {
            person = this.createNewPerson(doctorModel);
        }else {
            person = this.handleExistingPerson(personOptional.get(), doctorModel);
        }

        if (doctorRepository.existsByPersonId(person.getPersonId())) {
            throw new ClimedarException("DOCTOR_ALREADY_EXISTS", "Doctor already exists");
        }

        Doctor doctor = doctorMapper.toEntity(doctorModel);
        doctor.setPersonId(person.getPersonId());
        doctor.setSpeciality(specialityService.findById(doctorModel.getSpeciality().getId()));
        doctorRepository.save(doctor);

        return doctorMapper.toModel(doctor, person);
    }

    @Transactional
    public DoctorModel updateDoctor(Long id, DoctorModel doctorModel) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found" +
                " with id: " + id));

        Person personToUpdate = personRepository.findById(doctor.getPersonId());

        if (this.personDataMatcher(personToUpdate, doctorModel)) {
            personMapper.updatePerson(personToUpdate, doctorModel);
            personRepository.updatePerson(personToUpdate.getPersonId(), personToUpdate);
        }

        doctorMapper.updateEntity(doctor, doctorModel);
        doctorRepository.save(doctor);

        return doctorMapper.toModel(doctor, personToUpdate);
    }

    @Transactional
    public Boolean deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found" +
                " with id: " + id));

        doctor.setDeleted(true);
        doctorRepository.save(doctor);

        return true;
    }

    private Person handleExistingPerson(Person existingPerson, DoctorModel doctorModel) {
        if (existingPerson.isDeleted()) { //todo: verificar si eso realmente funciona
            Person updatedPerson = personMapper.toPerson(doctorModel);
            updatedPerson.setDeleted(false);
            return personRepository.updatePerson(existingPerson.getPersonId(), updatedPerson);
        }

        if (!personDataMatcher(existingPerson, doctorModel)) {
            throw new ClimedarException("PERSON_DATA_MISMATCH", "Person data mismatch");
        }

        return existingPerson;
    }

    private Person createNewPerson(DoctorModel doctorModel) {
        Person person = personMapper.toPerson(doctorModel);
        return personRepository.createPerson(person);
    }


    private boolean personDataMatcher(Person person, DoctorModel doctorModel) {
        return person.getName().equals(doctorModel.getName()) &&
                person.getSurname().equals(doctorModel.getSurname()) &&
                person.getDni().equals(doctorModel.getDni()) &&
                person.getEmail().equals(doctorModel.getEmail()) &&
                person.getPhone().equals(doctorModel.getPhone()) &&
                person.getBirthdate().equals(doctorModel.getBirthdate()) &&
                person.getAddress().equals(doctorModel.getAddress()) &&
                person.getGender().equals(doctorModel.getGender());
    }
}
