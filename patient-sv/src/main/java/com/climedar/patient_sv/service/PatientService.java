package com.climedar.patient_sv.service;

import com.climedar.patient_sv.entity.Patient;
import com.climedar.patient_sv.external.model.Gender;
import com.climedar.patient_sv.external.model.Person;
import com.climedar.patient_sv.mapper.PatientMapper;
import com.climedar.patient_sv.mapper.PersonMapper;
import com.climedar.patient_sv.repository.PatientRepository;
import com.climedar.patient_sv.repository.feign.PersonRepository;
import com.climedar.patient_sv.specification.PatientSpecification;
import com.climedar.library.exception.ClimedarException;
import com.climedar.patient_sv.model.PatientModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;


    public PatientModel getPatientById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient not found" +
                " with id: " + id));
        return patientMapper.toModel(patient, personRepository.findById(patient.getPersonId()));
    }

    public Page<PatientModel> getAllPatients(Pageable pageable, String fullName, String name,
                                                                         String surname, String dni,
                                                                         Gender gender, Long medicalSecureId) {


        Page<Person> personPage = personRepository.getAllPersons(pageable, fullName, name, surname, dni, gender);

        if (personPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Set<Long> personIds = personPage
                .getContent()
                .stream()
                .map(Person::getPersonId)
                .collect(Collectors.toSet());

        Specification<Patient> patientSpec = Specification
                .where(PatientSpecification.deletedEqual(false))
                .and(PatientSpecification.personIdIn(personIds))
                .and(PatientSpecification.medicalSecureIdEqual(medicalSecureId));

        List<Patient> patientList = patientRepository.findAll(patientSpec);

        if (patientList.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Map<Long, Patient> patientByPersonId = patientList
                .stream()
                .collect(Collectors.toMap(Patient::getPersonId, Function.identity()));

        List<PatientModel> finalPatientModels = new ArrayList<>();

        for (Person person : personPage.getContent()) {
            Patient patient = patientByPersonId.get(person.getPersonId());
            if (patient == null) {
                continue;
            }
            PatientModel model = patientMapper.toModel(patient, person);
            finalPatientModels.add(model);
        }

        return new PageImpl<>(finalPatientModels, pageable, personPage.getTotalElements());
    }

    @Transactional
    public PatientModel createPatient(PatientModel patientModel) {
        Optional<Person> personOptional = personRepository.findByDni(patientModel.getDni());
        Person person;
        if (personOptional.isEmpty()) {
            person = this.createNewPerson(patientModel);
        }else {
            person = this.handleExistingPerson(personOptional.get(), patientModel);
        }

        if (patientRepository.existsByPersonId(person.getPersonId())) {
            throw new ClimedarException("DOCTOR_ALREADY_EXISTS", "Patient already exists");
        }

        Patient patient = patientMapper.toEntity(patientModel);
        patient.setPersonId(person.getPersonId());
        patientRepository.save(patient);

        return patientMapper.toModel(patient, person);
    }

    @Transactional
    public PatientModel updatePatient(Long id, PatientModel patientModel) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient not found" +
                " with id: " + id));

        Person personToUpdate = personRepository.findById(patient.getPersonId());

        if (this.personDataMatcher(personToUpdate, patientModel)) {
            personMapper.updatePerson(personToUpdate, patientModel);
            personRepository.updatePerson(personToUpdate.getPersonId(), personToUpdate);
        }

        patientMapper.updateEntity(patient, patientModel);
        patientRepository.save(patient);

        return patientMapper.toModel(patient, personToUpdate);
    }

    @Transactional
    public Boolean deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient not found" +
                " with id: " + id));

        patient.setDeleted(true);
        patientRepository.save(patient);

        return true;
    }

    private Person handleExistingPerson(Person existingPerson, PatientModel patientModel) {
        if (existingPerson.isDeleted()) { //todo: verificar si eso realmente funciona
            Person updatedPerson = personMapper.toPerson(patientModel);
            updatedPerson.setDeleted(false);
            return personRepository.updatePerson(existingPerson.getPersonId(), updatedPerson);
        }

        return existingPerson;
    }

    private Person createNewPerson(PatientModel patientModel) {
        Person person = personMapper.toPerson(patientModel);
        return personRepository.createPerson(person);
    }


    private boolean personDataMatcher(Person person, PatientModel patientModel) {
        return person.getName().equals(patientModel.getName()) &&
                person.getSurname().equals(patientModel.getSurname()) &&
                person.getDni().equals(patientModel.getDni()) &&
                person.getEmail().equals(patientModel.getEmail()) &&
                person.getPhone().equals(patientModel.getPhone()) &&
                person.getBirthdate().equals(patientModel.getBirthdate()) &&
                person.getAddress().equals(patientModel.getAddress()) &&
                person.getGender().equals(patientModel.getGender());
    }

    public Patient getPatientEntityById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Patient not found" +
                " with id: " + id));
    }


    public Map<Long, PatientModel> getPatientsModelsFromPatients(Set<Patient> patients) {
        Set<Long> personIds = patients.stream()
                .map(Patient::getPersonId)
                .collect(Collectors.toSet());


        List<Person> persons = personRepository.findAllById(personIds);


        Map<Long, Person> personMap = persons.stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));


        return patients.stream()
                .collect(Collectors.toMap(
                        Patient::getId,
                        patient -> patientMapper.toModel(patient, personMap.get(patient.getPersonId()))
                ));
    }
}
