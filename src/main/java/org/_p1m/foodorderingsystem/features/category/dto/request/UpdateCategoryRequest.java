package org._p1m.foodorderingsystem.features.category.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCategoryRequest {

    @NotNull(message = "Name is required")
    private String name;
}
