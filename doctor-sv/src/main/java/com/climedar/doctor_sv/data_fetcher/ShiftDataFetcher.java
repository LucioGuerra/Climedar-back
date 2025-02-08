package com.climedar.doctor_sv.data_fetcher;

import com.climedar.doctor_sv.adapter.ShiftGraphqlAdapter;
import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.dto.response.ShiftPage;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.library.dto.request.PageRequestInput;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Controller
public class ShiftDataFetcher {

    private final ShiftGraphqlAdapter shiftAdapter;


    @QueryMapping
    public ShiftModel getShiftById(@Argument Long id) {
        return shiftAdapter.getShiftById(id);
    }

    @QueryMapping
    public ShiftPage getAllShifts(@Argument PageRequestInput pageRequest, @Argument ShiftSpecificationDTO specification) {
        return shiftAdapter.getAllShifts(pageRequest, specification);
    }

    @QueryMapping
    public Set<LocalDate> getDatesWithShifts(@Argument LocalDate fromDate,
                                             @Argument LocalDate toDate, @Argument Long doctorId) {
        return shiftAdapter.getDatesWithShifts(fromDate, toDate, doctorId);
    }

    @MutationMapping
    public ShiftModel createShift(@Argument CreateShiftDTO shift) {
        return shiftAdapter.createShift(shift);
    }

    @MutationMapping
    public ShiftModel updateShift(@Argument Long id, @Argument ShiftModel shift, @Argument ShiftSpecificationDTO specification) {
        return shiftAdapter.updateShift(id, shift, specification);
    }

    @MutationMapping
    public boolean deleteShift(@Argument Long id) {
        return shiftAdapter.deleteShift(id);
    }
}
