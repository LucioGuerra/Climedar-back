package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalPackageMapper {

    @Mapping(target = "price", expression = "java(entity.getPrice())")
    MedicalPackageModel toModel(MedicalPackageEntity entity);
}
