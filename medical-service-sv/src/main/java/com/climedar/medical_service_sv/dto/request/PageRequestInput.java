package com.climedar.medical_service_sv.dto.request;

import lombok.Data;

@Data
public class PageRequest {
    private int page;
    private int size;
    private SortOption sortOption;
}
