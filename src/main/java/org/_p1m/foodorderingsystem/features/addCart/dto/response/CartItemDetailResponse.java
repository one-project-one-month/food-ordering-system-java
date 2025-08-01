package org._p1m.foodorderingsystem.features.addCart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemDetailResponse {

    private Long cartId;
    private Integer quantity;

    private DishResponse dish;
    private ExtraResponse extra; // Will be null if no extra is selected
    private MenuResponse menu;
}