package com.climedar.payment_sv.dto.request;

import com.climedar.payment_sv.entity.revenue.RevenueType;
import com.climedar.payment_sv.external.model.medical_services.ServiceType;
import lombok.Data;

@Data
public class RevenueSpecificationDTO{
        private String date;
        private String fromDate;
        private String toDate;
        private RevenueType revenueType;
        private String specialityName;
        private ServiceType serviceType;
        private OriginName originName;

}
