package com.climedar.medical_service_sv.dto.request;

import lombok.Data;

@Data
public class SortOption {
    private String field;
    private SortDirection direction;
}
