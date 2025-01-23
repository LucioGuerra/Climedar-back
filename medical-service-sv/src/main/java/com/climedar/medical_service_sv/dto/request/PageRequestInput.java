package com.climedar.medical_service_sv.dto.request;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestInput {
    private int page;
    private int size;
    private SortOption sortOption;


    public Sort getSort() {
        Sort sort = Sort.unsorted();
        if (sortOption != null) {
            sort = Sort.by(
                    Sort.Direction.fromString(sortOption.getDirection().name()),
                    sortOption.getField()
            );
        }
        return sort;
    }
}
