package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalServiceMapper {
    MedicalServiceModel toModel(MedicalServiceEntity entity);

    MedicalServiceEntity toEntity(MedicalServiceModel dto);
}
