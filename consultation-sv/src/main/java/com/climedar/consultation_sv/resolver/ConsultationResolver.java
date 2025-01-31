package com.climedar.consultation_sv.resolver;

import com.climedar.consultation_sv.adapter.ConsultationGraphqlAdapter;
import com.climedar.consultation_sv.dto.request.ConsultationSpecificationDTO;
import com.climedar.consultation_sv.dto.request.CreateConsultationDTO;
import com.climedar.consultation_sv.dto.request.UpdateConsultationDTO;
import com.climedar.consultation_sv.dto.response.ConsultationPage;
import com.climedar.consultation_sv.model.ConsultationModel;
import com.climedar.library.dto.request.PageRequestInput;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class ConsultationResolver {

    private final ConsultationGraphqlAdapter consultationAdapter;

    @QueryMapping
    public ConsultationPage getAllConsultations(@Argument PageRequestInput pageRequest,
                                                @Argument ConsultationSpecificationDTO specification) {
        return consultationAdapter.getAllConsultations(pageRequest, specification);
    }

    @QueryMapping
    public ConsultationModel getConsultationById(@Argument Long id) {
        return consultationAdapter.getConsultationById(id);
    }

    @MutationMapping
    public ConsultationModel createConsultation(@Argument CreateConsultationDTO consultation) {
        return consultationAdapter.createConsultation(consultation);
    }

    @MutationMapping
    public ConsultationModel updateConsultation(@Argument Long id, @Argument UpdateConsultationDTO consultation) {
        return consultationAdapter.updateConsultation(id, consultation);
    }

    @MutationMapping
    public Boolean deleteConsultation(@Argument Long id) {
        return consultationAdapter.deleteConsultation(id);
    }
}
