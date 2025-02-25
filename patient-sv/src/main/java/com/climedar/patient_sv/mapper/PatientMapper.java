package com.climedar.patient_sv.mapper;

import com.climedar.patient_sv.entity.MedicalSecure;
import com.climedar.patient_sv.entity.Patient;
import com.climedar.patient_sv.external.model.Person;
import com.climedar.patient_sv.model.MedicalSecureModel;
import com.climedar.patient_sv.model.PatientModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MedicalSecureMapper.class})
public interface PatientMapper {


    @Mapping(target = "medicalSecure", expression = "java(this.getMedicalSecure(patientModel))")
    Patient toEntity(PatientModel doctorModel);

    @Mapping(target = "personId", expression = "java(person.getPersonId())")
    @Mapping(target = "medicalSecure" , source = "patient.medicalSecure")
    @Mapping(target = "deleted", ignore = true)
    PatientModel toModel(Patient patient, Person person);

    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "medicalSecure" , ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Patient patient, PatientModel patientModel);

    default MedicalSecureModel getMedicalSecure(PatientModel patientModel) {
        if (patientModel == null) {
            return null;
        }
        return patientModel.getMedicalSecure();
    }
}
