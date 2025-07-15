package org._p1m.foodorderingsystem.features.payment.service.impl;

import org._p1m.foodorderingsystem.features.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl {
    private final PaymentRepository paymentRepository;
    private final OrderDataRepository orderDataRepository;
}
