package com.climedar.payment_sv.adapter;

import com.climedar.library.dto.request.PageRequestInput;
import com.climedar.library.mapper.PageInfoMapper;
import com.climedar.payment_sv.dto.request.PaymentSpecificationDTO;
import com.climedar.payment_sv.dto.response.PaymentPage;
import com.climedar.payment_sv.model.PaymentModel;
import com.climedar.payment_sv.services.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class PaymentGraphqlAdapter {

    private final PaymentService paymentService;
    private final PageInfoMapper pageInfoMapper;

    public PaymentPage getAllPayments(PageRequestInput pageRequestInput, PaymentSpecificationDTO specification) {
        Pageable pageable = PageRequest.of(pageRequestInput.getPage()-1, pageRequestInput.getSize(), pageRequestInput.getSort());

        if (specification == null) {
            specification = new PaymentSpecificationDTO();
        }

        Page<PaymentModel> paymentModels = paymentService.getAllPayments(pageable, specification);

        PaymentPage paymentPage = new PaymentPage();
        paymentPage.setPayments(paymentModels.getContent());
        paymentPage.setPageInfo(pageInfoMapper.toPageInfo(paymentModels));
        return paymentPage;
    }

    public PaymentModel getPaymentById(Long id) {
        return paymentService.getPaymentById(id);
    }

    public Boolean cancelPayment(Long id) {
        return paymentService.cancelPayment(id);
    }
}
