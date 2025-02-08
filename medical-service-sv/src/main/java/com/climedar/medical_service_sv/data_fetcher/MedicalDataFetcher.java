package com.climedar.medical_service_sv.data_fetcher;

import com.climedar.medical_service_sv.adapter.GraphqlResolverAdapter;
import com.climedar.medical_service_sv.dto.request.PageRequestInput;
import com.climedar.medical_service_sv.dto.request.SpecificationDTO;
import com.climedar.medical_service_sv.dto.request.UpdateMedicalServiceDTO;
import com.climedar.medical_service_sv.dto.response.MedicalServicePage;
import com.climedar.medical_service_sv.model.MedicalServiceModel;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@DgsComponent
public class MedicalDataFetcher {

    private final GraphqlResolverAdapter graphqlAdapter;

    @DgsQuery
    public MedicalServiceModel getMedicalServiceById(@InputArgument Long id) {
        return graphqlAdapter.getMedicalServiceById(id);
    }

    @DgsQuery
    public MedicalServicePage getAllMedicalServices(@InputArgument PageRequestInput pageRequest, @InputArgument SpecificationDTO specification) {
        return graphqlAdapter.getAllMedicalServices(pageRequest, specification);
    }

    @DgsMutation
    public MedicalServiceModel createMedicalService(@InputArgument MedicalServiceModel input) {
        return graphqlAdapter.createMedicalService(input);
    }

    @DgsMutation
    public MedicalServiceModel updateMedicalService(@InputArgument Long id, @InputArgument UpdateMedicalServiceDTO input) {
        return graphqlAdapter.updateMedicalService(id, input);
    }

    @DgsMutation
    public Boolean deleteMedicalService(@InputArgument Long id) {
        return graphqlAdapter.deleteMedicalService(id);
    }
}
