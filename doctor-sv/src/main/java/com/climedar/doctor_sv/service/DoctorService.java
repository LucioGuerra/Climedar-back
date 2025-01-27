package com.climedar.doctor_sv.service;

import com.climedar.doctor_sv.dto.response.DoctorPage;
import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.external.model.Gender;
import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.mapper.DoctorMapper;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.repository.DoctorRepository;
import com.climedar.doctor_sv.repository.PersonRepository;
import com.climedar.doctor_sv.specification.DoctorSpecification;
import com.climedar.library.exception.ClimedarException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PersonRepository personRepository;
    private final SpecialityService specialityService;


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

        //todo: refactorizar y hacer que se junten todos los personId y luego hacer una sola consulta

        return doctors.map(doctor -> doctorMapper.toModel(doctor, personRepository.findById(doctor.getPersonId())));
    }

    @Transactional
    public DoctorModel createDoctor(DoctorModel doctorModel) {
        Optional<Person> person = personRepository.findByDni(doctorModel.getDni());

        if (person.isEmpty()) {
            Person newPerson = new Person(
                    doctorModel.getName(),
                    doctorModel.getSurname(),
                    doctorModel.getDni(),
                    doctorModel.getEmail(),
                    doctorModel.getPhone(),
                    doctorModel.getBirthdate(),
                    doctorModel.getAddress(),
                    doctorModel.getGender()
            );
            person = personRepository.createPerson(newPerson);
        }else if (person.get().isDeleted()) {
            Person newPerson = new Person(
                    doctorModel.getName(),
                    doctorModel.getSurname(),
                    doctorModel.getDni(),
                    doctorModel.getEmail(),
                    doctorModel.getPhone(),
                    doctorModel.getBirthdate(),
                    doctorModel.getAddress(),
                    doctorModel.getGender()
            );
            person = personRepository.updatePerson(person.get().getPersonId(), newPerson);
        } else if (!this.personDataMatcher(person.get(), doctorModel)) {
            throw new ClimedarException("PERSON_DATA_MISMATCH", "Person data mismatch");
        }

        if (doctorRepository.existsByPersonId(person.get().getPersonId())) {
            throw new ClimedarException("DOCTOR_ALREADY_EXIST", "Doctor already exists");
        }

        Doctor doctor = doctorMapper.toEntity(doctorModel);
        doctor.setSpeciality(specialityService.findById(doctorModel.getSpeciality().getId()));
        doctor.setPersonId(person.get().getPersonId());
        doctorRepository.save(doctor);

        return doctorMapper.toModel(doctor, person.get());
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
