package org._p1m.foodorderingsystem.features.addCart.dto.response;

import lombok.Data;

@Data
public class AddCartMenuResponse {
    private Long cartId;
    private Integer quantity;
    private Long customerId;
    private Long dishSizeId;
    private Long extraId;
}
