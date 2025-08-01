package org._p1m.foodorderingsystem.features.delivery.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplyDeliveryStaffRequest {
	@NotNull(message = "Order Id is required")
    private Long restaurantId;

    @NotNull(message = "Delivery Id is required")
    private Long userId;
}
