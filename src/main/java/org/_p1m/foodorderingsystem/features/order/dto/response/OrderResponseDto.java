package org._p1m.foodorderingsystem.features.order.dto.response;

import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponseDto {
    private Long id;
    private LocalDateTime orderDateTime;
    private Long addressId;
    private BigDecimal totalAmount;
    private DeliveryStatus deliveryStatus;
    private Long paymentId;
    private LocalDateTime createdAt;
}

