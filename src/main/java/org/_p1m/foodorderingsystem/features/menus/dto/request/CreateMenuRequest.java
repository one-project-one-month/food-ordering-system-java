package org._p1m.foodorderingsystem.features.menus.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateMenuRequest {

    @NotBlank(message = "Dish name is required")
    private String dish;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Dish image URL is required")
    private String dishImg;

    @NotNull(message = "Status is required")
    private Status status = Status.ACTIVE;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @Valid
    private List<DishSizeRequest> dishSizes;

    @Valid
    private List<ExtraRequest> extras;

    @Data
    public static class DishSizeRequest {

        @NotBlank(message = "Dish size name is required")
        private String name;

        @NotNull(message = "Dish size price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Dish size price must be greater than 0")
        private BigDecimal price;
    }

    @Data
    public static class ExtraRequest {

        @NotBlank(message = "Extra name is required")
        private String name;

        @NotNull(message = "Extra price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Extra price must be 0 or greater")
        private BigDecimal price;
    }


}

