package org._p1m.foodorderingsystem.features.delivery.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignDeliveryRequest {

    @NotNull(message = "Order Id is required")
    private Long orderId;

    @NotNull(message = "Delivery Id is required")
    private Long deliveryId;

    @NotNull(message = "Restaurant Id is required")
    private Long restaurantId;
}
