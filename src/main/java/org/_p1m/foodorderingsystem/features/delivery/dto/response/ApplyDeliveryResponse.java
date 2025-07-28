package org._p1m.foodorderingsystem.features.delivery.dto.response;

import org._p1m.foodorderingsystem.common.constant.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplyDeliveryResponse {
	Long restaurantId;
	Long deliveryId;
	Integer currentRestaruantApplied;
	Status status;
}
