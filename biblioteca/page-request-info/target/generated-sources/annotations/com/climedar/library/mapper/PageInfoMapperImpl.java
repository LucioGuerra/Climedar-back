package com.climedar.library.mapper;

import com.climedar.library.dto.response.PageInfo;
import javax.annotation.processing.Generated;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-24T23:51:35-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.11 (Oracle Corporation)"
)
@Component
public class PageInfoMapperImpl implements PageInfoMapper {

    @Override
    public PageInfo toPageInfo(Page<?> page) {
        if ( page == null ) {
            return null;
        }

        PageInfo pageInfo = new PageInfo();

        pageInfo.setCurrentPage( extractPageNumber( page ) );
        pageInfo.setTotalItems( extractTotalElements( page ) );
        pageInfo.setTotalElements( extractTotalElements( page ) );
        pageInfo.setTotalPages( extractTotalPages( page ) );
        pageInfo.setItemsPerPage( extractPageSize( page ) );
        pageInfo.setHasNextPage( extractHasNextPage( page ) );
        pageInfo.setHasPreviousPage( extractHasPreviousPage( page ) );

        return pageInfo;
    }
}
