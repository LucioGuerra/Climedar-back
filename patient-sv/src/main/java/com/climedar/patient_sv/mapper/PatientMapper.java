package com.climedar.patient_sv.mapper;

import com.climedar.patient_sv.entity.Patient;
import com.climedar.patient_sv.external.model.Person;
import com.climedar.patient_sv.model.PatientModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toEntity(PatientModel doctorModel);

    @Mapping(target = "personId", expression = "java(person.getPersonId())")
    @Mapping(target = "medicalSecure" , source = "patient.medicalSecure")
    @Mapping(target = "deleted", ignore = true)
    PatientModel toModel(Patient patient, Person person);

    @Mapping(target = "personId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Patient patient, PatientModel patientModel);
}
