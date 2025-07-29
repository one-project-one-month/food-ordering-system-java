package org._p1m.foodorderingsystem.features.processOrder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessOrderRequest {

    @NotNull(message = "Order ID cannot be null")
    private Long orderId;

    @NotNull(message = "Delivery Staff ID cannot be null")
    private Long deliveryStaffId;

    @NotNull(message = "Restaurant ID cannot be null")
    private Long restaurantId;
}