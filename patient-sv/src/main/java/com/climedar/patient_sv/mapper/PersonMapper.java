package com.climedar.patient_sv.mapper;

import com.climedar.patient_sv.external.model.Person;
import com.climedar.patient_sv.model.PatientModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    Person toPerson(PatientModel patientModel);

    void updatePerson(@MappingTarget Person person, PatientModel patientModel);
}
