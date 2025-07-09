package org._p1m.foodorderingsystem.features.menus.dto.request;


import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateMenuRequest {

    private String dish;

    private BigDecimal price;

    private String dishImg;

    private Status status = Status.ACTIVE;

    private Long restaurantId;

    private Long categoryId;

    private List<DishSize> dishSizes;

    private List<Extra> extras;


}

