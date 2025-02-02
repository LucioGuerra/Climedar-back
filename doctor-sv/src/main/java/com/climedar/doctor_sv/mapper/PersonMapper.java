package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.external.model.Person;
import com.climedar.doctor_sv.model.DoctorModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    Person toPerson(DoctorModel doctorModel);

    void updatePerson(@MappingTarget Person person, DoctorModel doctorModel);
}
