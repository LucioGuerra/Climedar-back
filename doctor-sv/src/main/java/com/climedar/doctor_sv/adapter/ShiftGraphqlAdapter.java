package com.climedar.doctor_sv.adapter;

import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.dto.response.DoctorPage;
import com.climedar.doctor_sv.dto.response.ShiftPage;
import com.climedar.doctor_sv.model.DoctorModel;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.service.ShiftService;
import com.climedar.doctor_sv.specification.ShiftSpecification;
import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ShiftGraphqlAdapter {

    private final ShiftService shiftService;
    private final PageInfoMapper pageInfoMapper;

    public ShiftModel createShift(CreateShiftDTO shift) {
        return shiftService.createShift(shift);
    }

    public ShiftModel getShiftById(Long id) {
        return shiftService.getShiftById(id);
    }

    public boolean deleteShift(Long id) {
        return shiftService.deleteShift(id);
    }

    public ShiftModel updateShift(Long id, ShiftModel shift, ShiftSpecificationDTO shiftSpecificationDTO) {
        return shiftService.updateShift(id, shift, shiftSpecificationDTO);
    }

    public ShiftPage getAllShifts(PageRequestInput pageRequest, ShiftSpecificationDTO specification) {
        Pageable pageable = PageRequest.of(pageRequest.getPage()-1, pageRequest.getSize(), pageRequest.getSort());

        Page<ShiftModel> shifts = shiftService.getAllShifts(pageable, specification);

        ShiftPage shiftPage = new ShiftPage();
        shiftPage.setShifts(shifts.getContent());
        shiftPage.setPageInfo(pageInfoMapper.toPageInfo(shifts));
        return shiftPage;

    }
}
