package com.climedar.doctor_sv.dto.response;

import coclimedar.library.dto.response.PageInfo;
import com.climedar.doctor_sv.model.SpecialityModel;
import lombok.Data;

import java.util.List;

@Data
public class SpecialityPage {
    private PageInfo pageInfo;
    private List<SpecialityModel> specialities;
}
