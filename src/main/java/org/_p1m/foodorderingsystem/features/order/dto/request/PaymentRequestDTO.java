package org._p1m.foodorderingsystem.features.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class PaymentRequestDTO {
    private String paymentScreenshot;
    private LocalDateTime dateTime;
    private Long userId;
    private Long deliveryUserId;
    private Long restaurantId;
    private Long couponId;
    private Long orderId;
}
