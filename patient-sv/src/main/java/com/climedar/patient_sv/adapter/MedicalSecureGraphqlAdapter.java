package com.climedar.patient_sv.adapter;


import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import com.climedar.patient_sv.dto.request.specification.MedicalSecureSpecificationDTO;
import com.climedar.patient_sv.dto.request.specification.PatientSpecificationDTO;
import com.climedar.patient_sv.dto.response.MedicalSecurePage;
import com.climedar.patient_sv.dto.response.PatientPage;
import com.climedar.patient_sv.model.MedicalSecureModel;
import com.climedar.patient_sv.model.PatientModel;
import com.climedar.patient_sv.service.MedicalSecureService;
import com.climedar.patient_sv.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MedicalSecureGraphqlAdapter {

    private final MedicalSecureService medicalSecureService;
    private final PageInfoMapper pageInfoMapper;

    public MedicalSecureModel getMedicalSecureById(Long id) {
        return medicalSecureService.getMedicalSecureById(id);
    }

    public MedicalSecureModel createMedicalSecure(MedicalSecureModel medSec) {
        return medicalSecureService.createMedicalSecure(medSec);
    }

    public MedicalSecureModel updateMedicalSecure(Long id, MedicalSecureModel medSec) {
        return medicalSecureService.updateMedicalSecure(id, medSec);
    }

    public boolean deleteMedicalSecure(Long id) {
        return medicalSecureService.deleteMedicalSecure(id);
    }

    public MedicalSecurePage getAllMedicalSecures(PageRequestInput pageRequestInput,
                                            MedicalSecureSpecificationDTO specification) {
        Pageable pageable = PageRequest.of(pageRequestInput.getPage()-1, pageRequestInput.getSize(), pageRequestInput.getSort());

        if (specification == null) {
            specification = new MedicalSecureSpecificationDTO();
        }

        Page<MedicalSecureModel> medSecs = medicalSecureService.getAllMedicalSecures(pageable, specification.getName());

        MedicalSecurePage medSecPage = new MedicalSecurePage();
        medSecPage.setMedicalSecures(medSecs.getContent());
        medSecPage.setPageInfo(pageInfoMapper.toPageInfo(medSecs));
        return medSecPage;

    }
}
