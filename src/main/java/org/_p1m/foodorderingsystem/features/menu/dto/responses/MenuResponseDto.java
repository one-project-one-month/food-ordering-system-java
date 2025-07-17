package org._p1m.foodorderingsystem.features.menu.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.Status;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @NoArgsConstructor
    public static class DishSizeDto {
        private Long id;
        private String name;
        private BigDecimal price;
        private String dishSizeImg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExtraDto {
        private Long id;
        private String name;
        private BigDecimal price;

    }
}


