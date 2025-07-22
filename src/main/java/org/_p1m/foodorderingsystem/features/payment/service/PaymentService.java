package org._p1m.foodorderingsystem.features.payment.service;

import org._p1m.foodorderingsystem.features.payment.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Long id);
}
