package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.model.DoctorModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SpecialityMapper.class})
public interface DoctorMapper {

    Doctor toEntity(DoctorModel doctorModel);

    @Mapping(target = "personId", expression = "java(person.getPersonId())")
    @Mapping(target = "speciality", source = "doctor.speciality")
    DoctorModel toModel(Doctor doctor, Person person);
}
