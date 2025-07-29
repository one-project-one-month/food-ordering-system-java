package org._p1m.foodorderingsystem.features.payment.service;

import org._p1m.foodorderingsystem.features.payment.dto.PaymentRequestDTO;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PaymentService {
    PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    PaymentResponseDTO getPaymentById(Long id);
    public String uploadPaymentPicture(final Long paymentId, final MultipartFile file);
}