package org._p1m.foodorderingsystem.features.payment.dto.response;

import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.Status;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponeseDTO {
    private Long paymentId;
    private Long orderId;
    private String paymentScreenshotUrl;
    private LocalDateTime paymentDateTime;
    private Status paymentStatus;
    private Status message;
}
