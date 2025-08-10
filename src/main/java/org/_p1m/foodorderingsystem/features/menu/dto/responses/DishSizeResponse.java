package org._p1m.foodorderingsystem.features.menu.dto.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishSizeResponse {

    private String name;

    private BigDecimal price;

    private Long menuId;
}
