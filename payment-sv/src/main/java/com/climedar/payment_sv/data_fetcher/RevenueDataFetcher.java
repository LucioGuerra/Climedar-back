package com.climedar.payment_sv.data_fetcher;

import com.climedar.payment_sv.dto.request.RevenueSpecificationDTO;
import com.climedar.payment_sv.dto.response.GetRevenueDTO;
import com.climedar.payment_sv.services.RevenueService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@AllArgsConstructor
@Controller
@DgsComponent
public class RevenueDataFetcher {

    private final RevenueService revenueService;

    @DgsQuery
    public List<GetRevenueDTO> getAllRevenues(@InputArgument RevenueSpecificationDTO specification) {
        return revenueService.getAllRevenue(specification);
    }

}
