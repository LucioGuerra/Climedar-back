package com.climedar.consultation_sv.dto.response;

import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.library.dto.response.PageInfo;
import lombok.Data;

import java.util.List;

@Data
public class ConsultationPage {
    PageInfo pageInfo;
    List<ConsultationModel> consultations;
}
