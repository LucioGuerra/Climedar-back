package com.climedar.doctor_sv.data_fetcher;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.doctor_sv.adapter.DoctorGraphqlAdapter;
import com.climedar.doctor_sv.dto.request.specification.DoctorSpecificationDTO;
import com.climedar.doctor_sv.dto.response.DoctorPage;
import com.climedar.doctor_sv.model.DoctorModel;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;

import java.util.Map;


@AllArgsConstructor
@DgsComponent
public class DoctorDataFetcher {

    private final DoctorGraphqlAdapter graphqlAdapter;

    @DgsQuery
    public DoctorModel getDoctorById(@InputArgument Long id) {
        return graphqlAdapter.getDoctorById(id);
    }

    @DgsQuery
    public DoctorPage getAllDoctors(@InputArgument PageRequestInput pageRequest,
                                    @InputArgument DoctorSpecificationDTO specification) {
        return graphqlAdapter.getAllDoctors(pageRequest, specification);
    }

    @DgsQuery
    public DoctorPage getDoctorsByFullName(@InputArgument PageRequestInput pageRequest,
                                           @InputArgument String fullName,
                                           @InputArgument Long specialityId) {
        return graphqlAdapter.getDoctorsByFullName(pageRequest, fullName, specialityId);
    }

    @DgsMutation
    public DoctorModel createDoctor(@InputArgument DoctorModel doctor) {
        return graphqlAdapter.createDoctor(doctor);
    }

    @DgsMutation
    public DoctorModel updateDoctor(@InputArgument Long id, @InputArgument DoctorModel doctor) {
        return graphqlAdapter.updateDoctor(id, doctor);
    }

    @DgsMutation
    public boolean deleteDoctor(@InputArgument Long id) {
        return graphqlAdapter.deleteDoctor(id);
    }

    @DgsEntityFetcher(name = "DoctorModel")
    public DoctorModel getDoctor(Map<String, Object> values) {
        Long id = Long.parseLong((String) values.get("id"));
        return graphqlAdapter.getDoctorById(id);
    }

}
