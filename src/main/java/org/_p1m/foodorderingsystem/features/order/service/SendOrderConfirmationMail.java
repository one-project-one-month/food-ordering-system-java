package org._p1m.foodorderingsystem.features.order.service;

public interface SendOrderConfirmationMail {
    void sendEmail(Long orderId);
}
