package org._p1m.foodorderingsystem.features.addCart.dto.request;

import lombok.Data;

@Data
public class AddCartMenuRequest {

    private Integer quantity;
    private Long customerId;
    private Long dishSizeId;
    private Long extraId;
}
