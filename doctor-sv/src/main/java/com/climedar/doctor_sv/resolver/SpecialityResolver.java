package com.climedar.doctor_sv.resolver;

import com.climedar.doctor_sv.adapter.SpecialityGraphqlAdapter;
import com.climedar.doctor_sv.dto.request.SpecialitySpecificationDTO;
import com.climedar.doctor_sv.dto.response.SpecialityPage;
import com.climedar.doctor_sv.model.SpecialityModel;
import com.climedar.library.dto.request.PageRequestInput;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class SpecialityResolver {

    private final SpecialityGraphqlAdapter specialityGraphqlAdapter;

    @QueryMapping
    public SpecialityModel getSpecialityById(@Argument Long id) {
        return specialityGraphqlAdapter.getSpecialityById(id);
    }

    @QueryMapping
    public SpecialityPage getAllSpecialities(@Argument PageRequestInput pageRequest, @Argument SpecialitySpecificationDTO specification) {
        return specialityGraphqlAdapter.getAllSpecialities(pageRequest, specification);
    }

    @MutationMapping
    public SpecialityModel createSpeciality(@Argument SpecialityModel speciality) {
        return specialityGraphqlAdapter.createSpeciality(speciality);
    }

    @MutationMapping
    public SpecialityModel updateSpeciality(@Argument Long id, @Argument SpecialityModel speciality) {
        return specialityGraphqlAdapter.updateSpeciality(id, speciality);
    }

    @MutationMapping
    public boolean deleteSpeciality(@Argument Long id) {
        return specialityGraphqlAdapter.deleteSpeciality(id);
    }
}
