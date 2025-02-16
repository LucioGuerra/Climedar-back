package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.entity.MedicalPackageEntity;
import com.climedar.medical_service_sv.entity.MedicalServiceEntity;
import com.climedar.medical_service_sv.external.model.Speciality;
import com.climedar.medical_service_sv.model.MedicalPackageModel;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MedicalServiceMapper.class})
public interface MedicalPackageMapper {

    @Mapping(target = "price", expression = "java(entity.getPrice())")
    @Mapping(target = "services", source = "services")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "estimatedDuration", expression = "java(entity.getEstimatedDuration())")
    @Mapping(target = "speciality", expression = "java(this.getSpeciality(entity))")
    MedicalPackageModel toModel(MedicalPackageEntity entity);


    default Speciality getSpeciality(MedicalPackageEntity entity) {
        return new Speciality(entity.getSpecialityId());
    }
}
