package com.climedar.consultation_sv.mapper;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.model.doctor.Shift;
import com.climedar.consultation_sv.external.model.medical_service.MedicalServices;
import com.climedar.consultation_sv.external.model.patient.Patient;
import com.climedar.consultation_sv.model.ConsultationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {


    @Mapping(target = "date", source = "shift.date")
    @Mapping(target = "finalPrice", source = "medicalServices.price")
    @Mapping(target = "doctor", source = "shift.doctor")
    @Mapping(target = "patient", source = "patient")
    @Mapping(target = "medicalService", source = "medicalServices")
    ConsultationModel toModel(Consultation consultation, Shift shift, MedicalServices medicalServices);

    Consultation toEntity(CreateConsultationDTO createConsultationDTO);

    void updateEntity(ConsultationModel consultationModel,@MappingTarget Consultation consultation);
}
