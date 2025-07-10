package org._p1m.foodorderingsystem.features.users.dto.request;

import jakarta.validation.constraints.Min;

public record DashboardRequestDto(
	    @Min(value = 1, message = "Page must be greater than 0") int page,
	    @Min(value = 1, message = "Size must be greater than 0") int size
	) {
	    public DashboardRequestDto {
	        if (page < 1) page = 1;
	        if (size < 1) size = 10;
	    }
	}
