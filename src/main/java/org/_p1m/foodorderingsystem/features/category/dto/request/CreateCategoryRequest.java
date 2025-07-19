package org._p1m.foodorderingsystem.features.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Restaurant Id is required")
    private Long RestaurantId;
}
