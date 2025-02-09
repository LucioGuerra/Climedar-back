package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.external.model.Speciality;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalServiceMapper {
    @Mapping(target = "speciality", expression = "java(this.getSpeciality(entity))")
    MedicalServiceModel toModel(MedicalServiceEntity entity);

    MedicalServiceEntity toEntity(MedicalServiceModel dto);

    default Speciality getSpeciality(MedicalServiceEntity entity) {
        return new Speciality(entity.getSpecialityId());
    }
}
