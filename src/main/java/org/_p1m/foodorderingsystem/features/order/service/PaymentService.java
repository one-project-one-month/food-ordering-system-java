package org._p1m.foodorderingsystem.features.order.service;

import org._p1m.foodorderingsystem.features.order.dto.request.PaymentRequestDTO;
import org._p1m.foodorderingsystem.features.order.dto.response.PaymentResponseDTO;
import org._p1m.foodorderingsystem.model.PaymentData;

import java.util.List;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);

    List<PaymentData> getAllPayments();
    PaymentData getPaymentById(Long id);
    void deletePayment(Long id);
}
