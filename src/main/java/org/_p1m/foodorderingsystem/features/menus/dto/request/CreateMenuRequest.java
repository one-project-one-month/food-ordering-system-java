package org._p1m.foodorderingsystem.features.menus.dto.request;


import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;

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

    private List<DishSizeRequest> dishSizes;

    private List<ExtraRequest> extras;

    @Data
    public static class DishSizeRequest {
        private String name;
        private BigDecimal price;
        private String dishSizeImg;
    }

    @Data
    public static class ExtraRequest {
        private String name;
        private BigDecimal price;
    }
}

