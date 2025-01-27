package coclimedar.library.dto.response;

import lombok.Data;

@Data
public class PageInfo {
    private int currentPage;
    private int totalItems;
    private int totalElements;
    private int totalPages;
    private int itemsPerPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
}
