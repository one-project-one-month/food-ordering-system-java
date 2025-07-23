package org._p1m.foodorderingsystem.features.order.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRequestDto {
    private LocalDateTime orderDateTime;
    private String userAddress;
    private BigDecimal totalAmount;
    private Long paymentId;
}

