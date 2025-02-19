package com.climedar.payment_sv.dto.response;

import com.climedar.library.dto.response.PageInfo;
import com.climedar.payment_sv.model.PaymentModel;
import lombok.Data;

import java.util.List;

@Data
public class PaymentPage {
    List<PaymentModel> payments;
    PageInfo pageInfo;
}
