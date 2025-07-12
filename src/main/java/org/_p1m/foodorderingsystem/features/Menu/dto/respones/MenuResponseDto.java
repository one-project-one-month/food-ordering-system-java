package org._p1m.foodorderingsystem.features.menu.dto.respones;

import lombok.AllArgsConstructor;
import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class MenuResponseDto {

    private Long id;

    private String dish;

    private BigDecimal price;

    private String dishImg;

    private Status status = Status.ACTIVE;

    private Long restaurantId;

    private Long categoryId;

    private List<DishSizeDto> dishSizes;

    private List<ExtraDto> extras;



    @Data
    @AllArgsConstructor
    public static class DishSizeDto {
        private Long id;
        private String name;
        private BigDecimal price;
        private String dishSizeImg;


    }

    @Data
    @AllArgsConstructor
    public static class ExtraDto {
        private Long id;
        private String name;
        private BigDecimal price;

    }
}


