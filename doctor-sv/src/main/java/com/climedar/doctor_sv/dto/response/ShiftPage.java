package com.climedar.doctor_sv.dto.response;

import com.climedar.library.dto.response.PageInfo;
import com.climedar.doctor_sv.model.ShiftModel;
import lombok.Data;

import java.util.List;

@Data
public class ShiftPage {
    private PageInfo pageInfo;
    private List<ShiftModel> shifts;
}
