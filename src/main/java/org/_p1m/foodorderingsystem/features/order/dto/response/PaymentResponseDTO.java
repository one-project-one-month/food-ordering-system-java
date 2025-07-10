package org._p1m.foodorderingsystem.features.order.dto.response;

import java.time.LocalDateTime;

public class PaymentResponseDTO {
    private Long id;
    private String paymentScreenshot;
    private LocalDateTime dateTime;
    private Long userId;
    private Long deliveryUserId;
    private Long restaurantId;
    private Long couponId;
    private Long orderId;
}
