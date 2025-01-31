package com.climedar.consultation_sv.adapter;

import com.climedar.consultation_sv.dto.request.ConsultationSpecificationDTO;
import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.dto.response.ConsultationPage;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.consultation_sv.service.ConsultationService;
import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ConsultationGraphqlAdapter {

    private final ConsultationService consultationService;
    private final PageInfoMapper pageInfoMapper;

    public ConsultationModel getConsultationById(Long id) {
        return consultationService.getConsultationById(id);
    }

    public ConsultationModel createConsultation(CreateConsultationDTO createConsultationDTO) {
        return consultationService.createConsultation(createConsultationDTO);
    }

    public ConsultationModel updateConsultation(Long id, UpdateConsultationDTO createConsultationDTO) {
        return consultationService.updateConsultation(id, createConsultationDTO);
    }

    public Boolean deleteConsultation(Long id) {
        return consultationService.deleteConsultation(id);
    }

    public ConsultationPage getAllConsultations(PageRequestInput pageRequestInput, ConsultationSpecificationDTO consultationSpecificationDTO) {
        Pageable pageable = PageRequest.of(pageRequestInput.getPage(), pageRequestInput.getSize(), pageRequestInput.getSort());

        Page<ConsultationModel> consultationModelPage = consultationService.getAllConsultations(pageable,
                consultationSpecificationDTO.patientId(), consultationSpecificationDTO.doctorId(),
                consultationSpecificationDTO.date(), consultationSpecificationDTO.startTime(),
                consultationSpecificationDTO.fromTime(), consultationSpecificationDTO.toTime(),
                consultationSpecificationDTO.medicalServiceId(), consultationSpecificationDTO.description(),
                consultationSpecificationDTO.observation());

        ConsultationPage consultationPage = new ConsultationPage();
        consultationPage.setPageInfo(pageInfoMapper.toPageInfo(consultationModelPage));
        consultationPage.setConsultations(consultationModelPage.getContent());
    }
}
