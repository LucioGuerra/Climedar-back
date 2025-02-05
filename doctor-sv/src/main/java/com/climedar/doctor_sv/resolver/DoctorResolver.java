package com.climedar.doctor_sv.resolver;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.doctor_sv.adapter.DoctorGraphqlAdapter;
import com.climedar.doctor_sv.dto.request.specification.DoctorSpecificationDTO;
import com.climedar.doctor_sv.dto.response.DoctorPage;
import com.climedar.doctor_sv.model.DoctorModel;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class DoctorResolver {

    private final DoctorGraphqlAdapter graphqlAdapter;

    @QueryMapping
    public DoctorModel getDoctorById(@Argument Long id) {
        return graphqlAdapter.getDoctorById(id);
    }

    @QueryMapping
    public DoctorPage getAllDoctors(@Argument PageRequestInput pageRequest,
                                    @Argument DoctorSpecificationDTO specification) {
        return graphqlAdapter.getAllDoctors(pageRequest, specification);
    }

    @QueryMapping
    public DoctorPage getDoctorsByFullName(@Argument PageRequestInput pageRequest,
                                           @Argument String fullName,
                                           @Argument Long specialityId) {
        return graphqlAdapter.getDoctorsByFullName(pageRequest, fullName, specialityId);
    }

    @MutationMapping
    public DoctorModel createDoctor(@Argument DoctorModel doctor) {
        return graphqlAdapter.createDoctor(doctor);
    }

    @MutationMapping
    public DoctorModel updateDoctor(@Argument Long id, @Argument DoctorModel doctor) {
        return graphqlAdapter.updateDoctor(id, doctor);
    }

    @MutationMapping
    public boolean deleteDoctor(@Argument Long id) {
        return graphqlAdapter.deleteDoctor(id);
    }

}
