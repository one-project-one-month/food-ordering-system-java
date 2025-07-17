package org._p1m.foodorderingsystem.features.menu.dto.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateDishSizeResponse {

    private String name;

    private BigDecimal price;

}
