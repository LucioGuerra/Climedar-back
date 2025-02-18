package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.dto.request.CreateMedicalDTO;
import com.climedar.medical_service_sv.external.model.Speciality;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;

@Mapper(componentModel = "spring")
public interface MedicalServiceMapper {
    @Mapping(target = "speciality", expression = "java(this.getSpeciality(entity))")
    MedicalServiceModel toModel(MedicalServiceEntity entity);

    @Mapping(target = "estimatedDuration", expression = "java(this.getDuration(dto.estimatedDuration()))")
    MedicalServiceEntity toEntity(CreateMedicalDTO dto);

    default Speciality getSpeciality(MedicalServiceEntity entity) {
        return new Speciality(entity.getSpecialityId());
    }

    default Duration getDuration(String duration) {
        return Duration.parse(duration);
    }
}
