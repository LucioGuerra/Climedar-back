package com.climedar.medical_service_sv.dto.request;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestInput {
    private int page;
    private int size;
    private SortOption order;


    public Sort getSort() {
        Sort sort = Sort.unsorted();
        if (order != null) {
            sort = Sort.by(
                    Sort.Direction.fromString(order.getDirection().name()),
                    order.getField()
            );
        }
        return sort;
    }
}
