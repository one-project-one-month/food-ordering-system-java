package org._p1m.foodorderingsystem.features.processOrder.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponseDTO {
    private Long id;
    private String itemName;
    private int quantity;
    private BigDecimal price;
}
