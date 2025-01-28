package com.climedar.doctor_sv.dto.response;

import com.climedar.library.dto.response.PageInfo;
import com.climedar.doctor_sv.model.DoctorModel;
import lombok.Data;

import java.util.List;

@Data
public class DoctorPage {
    private PageInfo pageInfo;
    private List<DoctorModel> doctors;
}
