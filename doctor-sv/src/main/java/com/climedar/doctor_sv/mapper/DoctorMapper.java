package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.entity.Doctor;
import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.model.DoctorModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {SpecialityMapper.class})
public interface DoctorMapper {

    Doctor toEntity(DoctorModel doctorModel);

    @Mapping(target = "personId", expression = "java(person.getPersonId())")
    @Mapping(target = "speciality", source = "doctor.speciality")
    @Mapping(target = "deleted", ignore = true)
    DoctorModel toModel(Doctor doctor, Person person);

    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "speciality", source = "doctor.speciality")
    void updateEntity(@MappingTarget Doctor doctor, DoctorModel doctorModel);
}
