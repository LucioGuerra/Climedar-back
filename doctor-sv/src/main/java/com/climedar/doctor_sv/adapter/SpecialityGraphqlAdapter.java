package com.climedar.doctor_sv.adapter;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import com.climedar.doctor_sv.dto.request.specification.SpecialitySpecificationDTO;
import com.climedar.doctor_sv.dto.response.SpecialityPage;
import com.climedar.doctor_sv.model.SpecialityModel;
import com.climedar.doctor_sv.service.SpecialityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SpecialityGraphqlAdapter {

    private final SpecialityService specialityService;
    private final PageInfoMapper pageInfoMapper;

    public SpecialityModel getSpecialityById(Long id) {
        return specialityService.getSpecialityById(id);
    }

    public SpecialityModel createSpeciality(SpecialityModel speciality) {
        return specialityService.createSpeciality(speciality);
    }

    public SpecialityModel updateSpeciality(Long id, SpecialityModel speciality) {
        return specialityService.updateSpeciality(id, speciality);
    }

    public boolean deleteSpeciality(Long id) {
        return specialityService.deleteSpeciality(id);
    }

    public SpecialityPage getAllSpecialities(PageRequestInput pageRequestInput, SpecialitySpecificationDTO specification) {
        Pageable pageable = PageRequest.of(pageRequestInput.getPage()-1, pageRequestInput.getSize(), pageRequestInput.getSort());

        if (specification == null) {
            specification = new SpecialitySpecificationDTO();
        }
        Page<SpecialityModel> specialities = specialityService.getAllSpecialities(pageable, specification.getName(),
                specification.getDescription(), specification.getCode());

        SpecialityPage specialityPage = new SpecialityPage();
        specialityPage.setSpecialities(specialities.getContent());
        specialityPage.setPageInfo(pageInfoMapper.toPageInfo(specialities));
        return specialityPage;
    }
}
