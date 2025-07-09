package org._p1m.foodorderingsystem.features.menus.dto.response;

import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateMenuResponseDto {

    private String dish;

    private BigDecimal price;

    private String dishImg;

    private Status status = Status.ACTIVE;

    private Long restaurantId;

    private Long categoryId;

    private List<DishSizeDto> dishSizes;

    private List<ExtraDto> extras;

    @Data
    public static class DishSizeDto {
        private String name;
        private BigDecimal price;
        private String dishSizeImg;
    }

    @Data
    public static class ExtraDto {
        private String name;
        private BigDecimal price;
    }

}
