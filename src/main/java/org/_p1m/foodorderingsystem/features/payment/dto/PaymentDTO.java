package org._p1m.foodorderingsystem.features.payment.dto;

import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private String paymentScreenshot;
    private LocalDateTime dateTime;
    private Status status;
    private Long userId;
    private Long deliverId;
    private Long resId;
    private Long couponId;
    private Long ratingId;
}
