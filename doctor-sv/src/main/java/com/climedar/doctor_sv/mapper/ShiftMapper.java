package com.climedar.doctor_sv.mapper;

import com.climedar.doctor_sv.entity.Shift;
import com.climedar.doctor_sv.model.ShiftModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class})
public interface ShiftMapper {
    Shift toEntity(ShiftModel shift);

    @Mapping(target = "doctor.shifts", ignore = true)
    @Mapping(target = "timeOfShifts", expression = "java(java.time.Duration.between(shift.getStartTime(), shift.getEndTime()))")
    ShiftModel toModel(Shift shift);

    void updateEntity(@MappingTarget Shift shift, ShiftModel shiftModel);
}

