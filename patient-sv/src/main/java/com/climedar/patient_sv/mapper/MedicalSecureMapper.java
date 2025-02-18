package com.climedar.patient_sv.mapper;

import com.climedar.patient_sv.entity.MedicalSecure;
import com.climedar.patient_sv.model.MedicalSecureModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalSecureMapper {

    MedicalSecure toEntity(MedicalSecureModel medicalSecureModel);

    MedicalSecureModel toModel(MedicalSecure medicalSecure);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget MedicalSecure medicalSecure, MedicalSecureModel medicalSecureModel);
}
