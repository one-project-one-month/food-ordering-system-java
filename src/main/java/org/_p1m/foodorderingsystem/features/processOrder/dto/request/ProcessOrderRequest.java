package org._p1m.foodorderingsystem.features.processOrder.dto.request;

import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.User;

@Getter
@Setter
public class ProcessOrderRequest {

    private OrderData orderData;
    private Long deliveryStaffId;
    private Long restaurantId;
}
