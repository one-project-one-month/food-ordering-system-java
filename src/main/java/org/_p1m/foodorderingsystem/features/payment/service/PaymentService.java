package org._p1m.foodorderingsystem.features.payment.service;

import org._p1m.foodorderingsystem.features.payment.dto.PaymentRequestDTO;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentById(Long id);
}