package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.model.ShiftModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShiftMapper {
    Shift toEntity(ShiftModel shift);
    ShiftModel toModel(Shift shift);
    void updateEntity(@MappingTarget Shift shift, ShiftModel shiftModel);
}

