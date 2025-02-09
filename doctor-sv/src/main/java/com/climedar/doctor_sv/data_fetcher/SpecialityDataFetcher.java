package com.climedar.doctor_sv.data_fetcher;

import com.climedar.doctor_sv.adapter.SpecialityGraphqlAdapter;
import com.climedar.doctor_sv.dto.request.specification.SpecialitySpecificationDTO;
import com.climedar.doctor_sv.dto.response.SpecialityPage;
import com.climedar.doctor_sv.model.SpecialityModel;
import com.climedar.library.dto.request.PageRequestInput;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@AllArgsConstructor
@DgsComponent
public class SpecialityDataFetcher {

    private final SpecialityGraphqlAdapter specialityGraphqlAdapter;

    @DgsQuery
    public SpecialityModel getSpecialityById(@InputArgument Long id) {
        return specialityGraphqlAdapter.getSpecialityById(id);
    }

    @DgsQuery
    public SpecialityPage getAllSpecialities(@InputArgument PageRequestInput pageRequest, @InputArgument SpecialitySpecificationDTO specification) {
        return specialityGraphqlAdapter.getAllSpecialities(pageRequest, specification);
    }

    @DgsMutation
    public SpecialityModel createSpeciality(@InputArgument SpecialityModel speciality) {
        return specialityGraphqlAdapter.createSpeciality(speciality);
    }

    @DgsMutation
    public SpecialityModel updateSpeciality(@InputArgument Long id, @InputArgument SpecialityModel speciality) {
        return specialityGraphqlAdapter.updateSpeciality(id, speciality);
    }

    @DgsMutation
    public Boolean deleteSpeciality(@InputArgument Long id) {
        return specialityGraphqlAdapter.deleteSpeciality(id);
    }

    @DgsEntityFetcher(name = "Speciality")
    public SpecialityModel getSpeciality(Map<String, Object> values) {
        Long id = ((Number) values.get("id")).longValue();
        return specialityGraphqlAdapter.getSpecialityById(id);
    }
}
