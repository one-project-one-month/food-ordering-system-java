package org._p1m.foodorderingsystem.features.menu.dto.request;

import jakarta.validation.constraints.Min;

public record GetAllMenuRequest(
        @Min(value = 1, message = "Page must be greater than 0") int page,
        @Min(value = 1, message = "Size must be greater than 0") int size
) {
    public GetAllMenuRequest(int page, int size) {
        this.page = Math.max(page, 1);
        this.size = (size < 1) ? 10 : size;
    }
}

