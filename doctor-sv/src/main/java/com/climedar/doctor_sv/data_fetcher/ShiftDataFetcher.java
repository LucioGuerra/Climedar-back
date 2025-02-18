package com.climedar.doctor_sv.data_fetcher;

import com.climedar.doctor_sv.adapter.ShiftGraphqlAdapter;
import com.climedar.doctor_sv.dto.request.CreateShiftDTO;
import com.climedar.doctor_sv.dto.request.specification.ShiftSpecificationDTO;
import com.climedar.doctor_sv.dto.response.ShiftPage;
import com.climedar.doctor_sv.model.ShiftModel;
import com.climedar.library.dto.request.PageRequestInput;
import com.netflix.graphql.dgs.*;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@DgsComponent
public class ShiftDataFetcher {

    private final ShiftGraphqlAdapter shiftAdapter;


    @DgsQuery
    public ShiftModel getShiftById(@InputArgument Long id) {
        return shiftAdapter.getShiftById(id);
    }

    @DgsQuery
    public ShiftPage getAllShifts(@InputArgument PageRequestInput pageRequest, @InputArgument ShiftSpecificationDTO specification) {
        return shiftAdapter.getAllShifts(pageRequest, specification);
    }

    @DgsQuery
    public Set<LocalDate> getDatesWithShifts(@InputArgument String fromDate,
                                             @InputArgument String toDate, @InputArgument Long doctorId) {
        return shiftAdapter.getDatesWithShifts(fromDate, toDate, doctorId);
    }

    @DgsMutation
    public Integer createShift(@InputArgument CreateShiftDTO shift) {
        return shiftAdapter.createShift(shift);
    }

    @DgsMutation
    public ShiftModel updateShift(@InputArgument Long id, @InputArgument ShiftModel shift, @InputArgument ShiftSpecificationDTO specification) {
        return shiftAdapter.updateShift(id, shift, specification);
    }

    @DgsMutation
    public boolean deleteShift(@InputArgument Long id) {
        return shiftAdapter.deleteShift(id);
    }

    @DgsEntityFetcher(name = "ShiftModel")
    public ShiftModel getShift(Map<String, Object> values) {
        Long id = Long.parseLong((String) values.get("id"));
        return shiftAdapter.getShiftById(id);
    }
}
