package org._p1m.foodorderingsystem.features.delivery.dto.request;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeDeliveryStatusRequest {
	@NotNull(message = "Delivery Status is required")
    private DeliveryStatus deliveryStatus;
	
	@NotNull(message = "Order id cannot be null")
	private Long orderId;
}
