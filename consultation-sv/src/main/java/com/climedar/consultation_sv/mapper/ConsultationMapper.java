package com.climedar.consultation_sv.mapper;

import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.model.ConsultationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    ConsultationModel toModel(Consultation consultation);

    Consultation toEntity(CreateConsultationDTO createConsultationDTO);

    void updateEntity(ConsultationModel consultationModel,@MappingTarget Consultation consultation);
}
