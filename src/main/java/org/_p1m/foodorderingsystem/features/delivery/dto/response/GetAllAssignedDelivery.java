package org._p1m.foodorderingsystem.features.delivery.dto.response;

import lombok.Data;

@Data
public class GetAllAssignedDelivery {
	private Long orderId;
    private String restaurantName;
    private String customerAddress;
}
