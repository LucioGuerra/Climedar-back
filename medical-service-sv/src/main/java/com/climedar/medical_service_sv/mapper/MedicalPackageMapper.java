package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalPackageMapper {
    MedicalPackageModel toModel(MedicalPackageEntity entity);

    MedicalPackageEntity toEntity(MedicalPackageModel dto);
}
