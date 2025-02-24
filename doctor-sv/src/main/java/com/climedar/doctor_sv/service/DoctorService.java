package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.dto.response.DoctorPage;
import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.external.model.Gender;
import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.mapper.DoctorMapper;
import com.climedar.doctor_sv.mapper.PersonMapper;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.repository.DoctorRepository;
import com.climedar.doctor_sv.repository.feign.PersonRepository;
import com.climedar.doctor_sv.specification.DoctorSpecification;
import com.climedar.library.dto.response.PageInfo;
import com.climedar.library.exception.ClimedarException;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PersonRepository personRepository;
    private final SpecialityService specialityService;
    private final PersonMapper personMapper;


    public DoctorModel getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found" +
                " with id: " + id));
        Person person = personRepository.findById(doctor.getPersonId());
        DoctorModel model = doctorMapper.toModel(doctor, person);
        return model;
    }

    public DoctorPage getAllDoctors(Pageable pageable, String fullName, String name,
                                    String surname, String dni,
                                    Gender gender, Long shiftId,
                                    Long specialityId) {


        List<Person> personPage = personRepository.getAllPersons(fullName, name, surname, dni, gender);

        if (personPage.isEmpty()) {
            return new DoctorPage();
        }

        Set<Long> personIds = personPage.stream()
                .map(Person::getPersonId)
                .collect(Collectors.toSet());

        Specification<Doctor> doctorSpec = Specification
                .where(DoctorSpecification.deletedEqual(false))
                .and(DoctorSpecification.personIdIn(personIds))
                .and(DoctorSpecification.specialityIdEqual(specialityId))
                .and(DoctorSpecification.shiftIdEqual(shiftId));

        List<Doctor> doctorList = doctorRepository.findAll(doctorSpec);

        if (doctorList.isEmpty()) {
            return new DoctorPage();
        }

        Map<Long, Doctor> doctorByPersonId = doctorList.stream()
                .collect(Collectors.toMap(Doctor::getPersonId, Function.identity()));

        List<DoctorModel> doctorModels = new ArrayList<>();


        for (Person person : personPage) {
            Doctor doctor = doctorByPersonId.get(person.getPersonId());
            if (doctor != null) {
                DoctorModel model = doctorMapper.toModel(doctor, person);
                doctorModels.add(model);
            }
        }

        int totalItems = doctorModels.size();
        int itemsPerPage = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int totalPages = totalItems / itemsPerPage;

        List<DoctorModel> content = new ArrayList<>();
        for (int i = 0; i < itemsPerPage && i < totalItems; i++) {
            content.add(doctorModels.get((currentPage * itemsPerPage) + i));
        }

        DoctorPage doctorPage = new DoctorPage();
        doctorPage.setDoctors(content);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalItems(totalItems);
        pageInfo.setItemsPerPage(itemsPerPage);
        pageInfo.setCurrentPage(currentPage+1);
        pageInfo.setTotalPages(totalPages);
        doctorPage.setPageInfo(pageInfo);

        return doctorPage;
    }

    public Page<DoctorModel> getDoctorsByFullName(String fullName, Long specialityId, Pageable pageable) {

        Page<Person> personPage = personRepository.getAllPersonsByFullName(pageable, fullName);

        if (personPage.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Set<Long> personIds = personPage
                .getContent()
                .stream()
                .map(Person::getPersonId)
                .collect(Collectors.toSet());

        Specification<Doctor> doctorSpec = Specification
                .where(DoctorSpecification.deletedEqual(false))
                .and(DoctorSpecification.personIdIn(personIds))
                .and(DoctorSpecification.specialityIdEqual(specialityId));

        List<Doctor> doctorList = doctorRepository.findAll(doctorSpec);

        if (doctorList.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Map<Long, Doctor> doctorByPersonId = doctorList
                .stream()
                .collect(Collectors.toMap(Doctor::getPersonId, Function.identity()));

        List<DoctorModel> finalDoctorModels = new ArrayList<>();

        for (Person person : personPage.getContent()) {
            Doctor doctor = doctorByPersonId.get(person.getPersonId());
            if (doctor == null) {
                continue;
            }
            DoctorModel model = doctorMapper.toModel(doctor, person);
            finalDoctorModels.add(model);
        }

        return new PageImpl<>(finalDoctorModels, pageable, personPage.getTotalElements());
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
        doctor.setSpeciality(specialityService.findEntityById(doctorModel.getSpeciality().getId()));
        doctorRepository.save(doctor);

        return doctorMapper.toModel(doctor, person);
    }

    @Transactional
    public DoctorModel updateDoctor(Long id, DoctorModel doctorModel) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found" +
                " with id: " + id));

        Person personToUpdate = personRepository.findById(doctor.getPersonId());

        Person person;
        if (!this.personDataMatcher(personToUpdate, doctorModel)) {
            personMapper.updatePerson(personToUpdate, doctorModel);
            person = personRepository.updatePerson(doctor.getPersonId(), personToUpdate);
        }
        else {
            person = personToUpdate;
        }

        doctorMapper.updateEntity(doctor, doctorModel);

        if (doctorModel.getSpeciality() != null) {
            doctor.setSpeciality(specialityService.findEntityById(doctorModel.getSpeciality().getId()));
        }

        doctorRepository.save(doctor);

        return doctorMapper.toModel(doctor, person);
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

    public Doctor getDoctorEntityById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Doctor not found" +
                " with id: " + id));
    }


    public Map<Long, DoctorModel> getDoctorsModelsFromDoctors(Set<Doctor> doctors) {
        Set<Long> personIds = doctors.stream()
                .map(Doctor::getPersonId)
                .collect(Collectors.toSet());


        List<Person> persons = personRepository.findAllById(personIds);


        Map<Long, Person> personMap = persons.stream()
                .collect(Collectors.toMap(Person::getPersonId, Function.identity()));


        return doctors.stream()
                .collect(Collectors.toMap(
                        Doctor::getId,
                        doctor -> doctorMapper.toModel(doctor, personMap.get(doctor.getPersonId()))
                ));
    }
}
