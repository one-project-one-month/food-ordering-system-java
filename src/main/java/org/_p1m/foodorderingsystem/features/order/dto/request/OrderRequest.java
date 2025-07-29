package org._p1m.foodorderingsystem.features.order.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderRequest {
    private LocalDateTime orderDateTime;
    private Long addressId;
    private BigDecimal totalAmount;
    private Long customerId;
}
