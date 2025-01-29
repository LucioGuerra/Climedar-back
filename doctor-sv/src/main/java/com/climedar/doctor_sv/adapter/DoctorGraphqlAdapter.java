package com.climedar.doctor_sv.adapter;


import com.climedar.doctor_sv.dto.request.specification.DoctorSpecificationDTO;
import com.climedar.doctor_sv.dto.response.DoctorPage;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.service.DoctorService;
import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DoctorGraphqlAdapter {

    private final DoctorService doctorService;
    private final PageInfoMapper pageInfoMapper;

    public DoctorModel getDoctorById(Long id) {
        return doctorService.getDoctorById(id);
    }

    public DoctorModel createDoctor(DoctorModel doctor) {
        return doctorService.createDoctor(doctor);
    }

    public DoctorModel updateDoctor(Long id, DoctorModel doctor) {
        return doctorService.updateDoctor(id, doctor);
    }

    public boolean deleteDoctor(Long id) {
        return doctorService.deleteDoctor(id);
    }

    public DoctorPage getAllDoctors(PageRequestInput pageRequestInput, DoctorSpecificationDTO specification) {
        Pageable pageable = PageRequest.of(pageRequestInput.getPage()-1, pageRequestInput.getSize(), pageRequestInput.getSort());

        Page<DoctorModel> doctors = doctorService.getAllDoctors(pageable, specification.getName(),
                specification.getSurname(), specification.getDni(), specification.getGender(),
                specification.getShiftId(), specification.getSpecialtyId());

        DoctorPage doctorPage = new DoctorPage();
        doctorPage.setDoctors(doctors.getContent());
        doctorPage.setPageInfo(pageInfoMapper.toPageInfo(doctors));
        return doctorPage;

    }

}
