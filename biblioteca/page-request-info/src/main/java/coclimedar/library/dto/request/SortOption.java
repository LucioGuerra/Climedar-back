package coclimedar.library.dto.request;

import lombok.Data;

@Data
public class SortOption {
    private String field;
    private SortDirection direction;
}