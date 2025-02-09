package com.climedar.patient_sv.data_fetcher;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.patient_sv.adapter.PatientGraphqlAdapter;
import com.climedar.patient_sv.dto.request.specification.PatientSpecificationDTO;
import com.climedar.patient_sv.dto.response.PatientPage;
import com.climedar.patient_sv.model.PatientModel;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.Map;

@AllArgsConstructor
@Controller
@DgsComponent
public class PatientDataFetcher {

    private final PatientGraphqlAdapter graphqlAdapter;

    @DgsQuery
    public PatientModel getPatientById(@InputArgument Long id) {
        return graphqlAdapter.getPatientById(id);
    }

    @DgsQuery
    public PatientPage getAllPatients(@InputArgument PageRequestInput pageRequest,
                                                                         @InputArgument PatientSpecificationDTO specification) {
        return graphqlAdapter.getAllPatients(pageRequest, specification);
    }

    @DgsMutation
    public PatientModel createPatient(@InputArgument PatientModel patient) {
        return graphqlAdapter.createPatient(patient);
    }

    @DgsMutation
    public PatientModel updatePatient(@InputArgument Long id, @InputArgument PatientModel patient) {
        return graphqlAdapter.updatePatient(id, patient);
    }

    @DgsMutation
    public boolean deletePatient(@InputArgument Long id) {
        return graphqlAdapter.deletePatient(id);
    }

    @DgsEntityFetcher(name = "Patient")
    public PatientModel getPatient(Map<String, Object> values) {
        Long id = ((Number) values.get("id")).longValue();
        return graphqlAdapter.getPatientById(id);
    }

}
