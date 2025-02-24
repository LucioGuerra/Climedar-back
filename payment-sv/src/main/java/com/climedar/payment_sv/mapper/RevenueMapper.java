package com.climedar.payment_sv.mapper;

import com.climedar.payment_sv.dto.response.RevenuePieChartDTO;
import com.climedar.payment_sv.entity.revenue.Revenue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RevenueMapper {

    @Mapping(target = "name", source = "specialityName")
    @Mapping(target = "value", source = "amount")
    RevenuePieChartDTO toDTOSpecialityName(Revenue revenue);

    @Mapping(target = "name", expression = "java(this.translateServiceType(revenue))")
    @Mapping(target = "value", source = "amount")
    RevenuePieChartDTO toDTOServiceType(Revenue revenue);

    default String translateServiceType(Revenue revenue) {
        return switch (revenue.getMedicalServiceType()) {
            case GENERAL -> "Consulta General";
            case SPECIALIST -> "Consulta Especializada";
            case SURGERY -> "Cirugía";
            case EXAMS -> "Exámen médico";
            case THERAPY -> "Terapia y tratamientos varios";
        };
    }
}
