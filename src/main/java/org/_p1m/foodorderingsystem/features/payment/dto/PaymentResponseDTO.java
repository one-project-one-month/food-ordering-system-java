package org._p1m.foodorderingsystem.features.payment.dto;

import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.Status;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponseDTO {

    private Long id;
    private Status status;
    private LocalDateTime dateTime;
    private String paymentMethod;
    private String paymentScreenshot;
    private Long userId;
    private Long restaurantId;
    private Long couponId;
}