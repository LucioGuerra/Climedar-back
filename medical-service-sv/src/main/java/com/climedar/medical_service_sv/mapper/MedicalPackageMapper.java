package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MedicalServiceMapper.class})
public interface MedicalPackageMapper {

    @Mapping(target = "price", expression = "java(entity.getPrice())")
    @Mapping(target = "services", source = "services")
    @Mapping(target = "name", source = "name")
    MedicalPackageModel toModel(MedicalPackageEntity entity);
}
