package com.climedar.patient_sv.mapper;

import com.climedar.patient_sv.entity.MedicalSecure;
import com.climedar.patient_sv.model.MedicalSecureModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalSecureMapper {

    MedicalSecure toEntity(MedicalSecureModel medicalSecureModel);

    MedicalSecureModel toModel(MedicalSecure medicalSecure);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget MedicalSecure medicalSecure, MedicalSecureModel medicalSecureModel);
}
