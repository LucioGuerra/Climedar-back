package com.climedar.doctor_sv.resolver;

import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.doctor_sv.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class ShiftResolver {

    private final ShiftService shiftService;


    @MutationMapping
    public ShiftModel createShift(@Argument CreateShiftDTO shift) {
        return shiftService.createShift(shift);
    }
}
