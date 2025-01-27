package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.entity.Speciality;
import com.climedar.doctor_sv.model.SpecialityModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    SpecialityModel toModel(Speciality speciality);
}
