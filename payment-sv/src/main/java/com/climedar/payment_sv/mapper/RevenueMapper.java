package com.climedar.payment_sv.mapper;

import com.climedar.payment_sv.dto.response.RevenuePieChartDTO;
import com.climedar.payment_sv.entity.revenue.Revenue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RevenueMapper {

    @Mapping(target = "name", source = "specialityName")
    @Mapping(target = "amount", source = "amount")
    RevenuePieChartDTO toDTOSpecialityName(Revenue revenue);

    @Mapping(target = "name", source = "medicalServiceType")
    @Mapping(target = "amount", source = "amount")
    RevenuePieChartDTO toDTOServiceType(Revenue revenue);
}
