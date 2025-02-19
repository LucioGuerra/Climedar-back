package com.climedar.payment_sv.data_fetcher;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.payment_sv.adapter.PaymentGraphqlAdapter;
import com.climedar.payment_sv.dto.request.PaymentSpecificationDTO;
import com.climedar.payment_sv.dto.response.PaymentPage;
import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.model.PaymentModel;
import com.climedar.payment_sv.services.PaymentService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;

@AllArgsConstructor
@Controller
@DgsComponent
public class PaymentDataFetcher {

    private final PaymentGraphqlAdapter paymentGraphqlAdapter;

    @DgsQuery
    public PaymentPage getAllPayments(@InputArgument PageRequestInput pageRequest,
                                      @InputArgument PaymentSpecificationDTO specification) {
        return paymentGraphqlAdapter.getAllPayments(pageRequest, specification);
    }

    @DgsQuery
    public PaymentModel getPaymentById(@InputArgument Long id) {
        return paymentGraphqlAdapter.getPaymentById(id);
    }
}
