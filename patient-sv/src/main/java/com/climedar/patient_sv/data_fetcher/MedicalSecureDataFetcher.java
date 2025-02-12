package com.climedar.patient_sv.data_fetcher;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.patient_sv.adapter.MedicalSecureGraphqlAdapter;
import com.climedar.patient_sv.dto.request.specification.MedicalSecureSpecificationDTO;
import com.climedar.patient_sv.dto.response.MedicalSecurePage;
import com.climedar.patient_sv.model.MedicalSecureModel;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@AllArgsConstructor
@Controller
@DgsComponent
public class MedicalSecureDataFetcher {

    MedicalSecureGraphqlAdapter medicalSecureGraphqlAdapter;

    @DgsQuery
    public MedicalSecureModel getMedicalSecureById(@InputArgument Long id) {
        return medicalSecureGraphqlAdapter.getMedicalSecureById(id);
    }

    @DgsQuery
    public MedicalSecurePage getAllMedicalSecures(@InputArgument PageRequestInput pageRequest, @InputArgument MedicalSecureSpecificationDTO specification) {
        return medicalSecureGraphqlAdapter.getAllMedicalSecures(pageRequest, specification);
    }

    @DgsMutation
    public MedicalSecureModel createMedicalSecure(@InputArgument MedicalSecureModel medicalSecure) {
        return medicalSecureGraphqlAdapter.createMedicalSecure(medicalSecure);
    }

    @DgsMutation
    public MedicalSecureModel updateMedicalSecure(@InputArgument Long id, @InputArgument MedicalSecureModel medicalSecure) {
        return medicalSecureGraphqlAdapter.updateMedicalSecure(id, medicalSecure);
    }

    @DgsMutation
    public Boolean deleteMedicalSecure(@InputArgument Long id) {
        return medicalSecureGraphqlAdapter.deleteMedicalSecure(id);
    }

    @DgsEntityFetcher(name = "MedicalSecureModel")
    public MedicalSecureModel getMedicalSecure(Map<String, Object> values) {
        Long id = Long.parseLong((String) values.get("id"));
        return medicalSecureGraphqlAdapter.getMedicalSecureById(id);
    }
}
