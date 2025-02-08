package com.climedar.patient_sv.resolver;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.patient_sv.adapter.PatientGraphqlAdapter;
import com.climedar.patient_sv.dto.request.specification.PatientSpecificationDTO;
import com.climedar.patient_sv.dto.response.PatientPage;
import com.climedar.patient_sv.model.PatientModel;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class PatientResolver {

    private final PatientGraphqlAdapter graphqlAdapter;

    @QueryMapping
    public PatientModel getPatientById(@Argument Long id) {
        return graphqlAdapter.getPatientById(id);
    }

    @QueryMapping
    public PatientPage getAllPatients(@Argument PageRequestInput pageRequest,
                                                                         @Argument PatientSpecificationDTO specification) {
        return graphqlAdapter.getAllPatients(pageRequest, specification);
    }

    @MutationMapping
    public PatientModel createPatient(@Argument PatientModel patient) {
        return graphqlAdapter.createPatient(patient);
    }

    @MutationMapping
    public PatientModel updatePatient(@Argument Long id, @Argument PatientModel patient) {
        return graphqlAdapter.updatePatient(id, patient);
    }

    @MutationMapping
    public boolean deletePatient(@Argument Long id) {
        return graphqlAdapter.deletePatient(id);
    }

}
