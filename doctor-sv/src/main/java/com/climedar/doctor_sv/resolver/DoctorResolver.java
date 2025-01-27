package com.climedar.doctor_sv.resolver;

import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class DoctorResolver {

    private final DoctorService doctorService;

    @QueryMapping
    public DoctorModel getDoctorById(@Argument Long id) {
        return doctorService.getDoctorById(id);
    }

    @MutationMapping
    public DoctorModel createDoctor(@Argument DoctorModel doctor) {
        return doctorService.createDoctor(doctor);
    }

}
