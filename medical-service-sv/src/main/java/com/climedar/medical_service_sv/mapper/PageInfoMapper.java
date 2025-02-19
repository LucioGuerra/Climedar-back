package com.climedar.medical_service_sv.mapper;

import com.climedar.medical_service_sv.dto.response.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PageInfoMapper {
    @Mapping(source = "page", target = "currentPage", qualifiedByName = "extractPageNumber")
    @Mapping(source = "page", target = "totalItems", qualifiedByName = "extractTotalElements")
    @Mapping(source = "page", target = "totalElements", qualifiedByName = "extractTotalElements")
    @Mapping(source = "page", target = "totalPages", qualifiedByName = "extractTotalPages")
    @Mapping(source = "page", target = "itemsPerPage", qualifiedByName = "extractPageSize")
    @Mapping(source = "page", target = "hasNextPage", qualifiedByName = "extractHasNextPage")
    @Mapping(source = "page", target = "hasPreviousPage", qualifiedByName = "extractHasPreviousPage")
    PageInfo toPageInfo(Page<?> page);

    @Named("extractPageNumber")
    default int extractPageNumber(Page<?> page) {
        return page.getNumber() + 1;
    }

    @Named("extractTotalElements")
    default int extractTotalElements(Page<?> page) {
        return (int) page.getTotalElements();
    }

    @Named("extractTotalPages")
    default int extractTotalPages(Page<?> page) {
        return page.getTotalPages();
    }

    @Named("extractPageSize")
    default int extractPageSize(Page<?> page) {
        return page.getSize();
    }

    @Named("extractHasNextPage")
    default boolean extractHasNextPage(Page<?> page) {
        return page.hasNext();
    }

    @Named("extractHasPreviousPage")
    default boolean extractHasPreviousPage(Page<?> page) {
        return page.hasPrevious();
    }
}
