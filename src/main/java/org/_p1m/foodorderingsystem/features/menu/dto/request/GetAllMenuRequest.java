package org._p1m.foodorderingsystem.features.menu.dto.request;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record GetAllMenuRequest(
        @Min(value = 1, message = "Page must be greater than 0") Integer page,
        @Min(value = 1, message = "Size must be greater than 0") Integer size,
        String dish,
        BigDecimal price,
        Long categoryId,
        String status
) {
    public GetAllMenuRequest {
        page = (page == null || page < 1) ? 1 : page;
        size = (size == null || size < 1) ? 10 : size;
    }
}

