package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.model.SpecialityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    SpecialityModel toModel(Speciality speciality);

    @Mapping(target = "id", ignore = true)
    Speciality toEntity(SpecialityModel specialityModel);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Speciality speciality, SpecialityModel specialityModel);
}
