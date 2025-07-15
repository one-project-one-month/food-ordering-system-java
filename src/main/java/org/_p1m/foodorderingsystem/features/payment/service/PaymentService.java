package org._p1m.foodorderingsystem.features.payment.service;

import org._p1m.foodorderingsystem.features.payment.dto.request.PaymentCreateDTO;
import org._p1m.foodorderingsystem.features.payment.dto.response.PaymentResponeseDTO;

public interface PaymentService {
    PaymentResponeseDTO createPayment(PaymentCreateDTO paymentCreateDTO);
}
