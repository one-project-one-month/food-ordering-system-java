package org._p1m.foodorderingsystem.features.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDTO {

    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Restaurant ID cannot be null")
    private Long restaurantId;

    private Long couponId;

    @NotNull(message = "Payment Method cannot be null")
    private String paymentMethod;

    private String paymentScreenshot;
}