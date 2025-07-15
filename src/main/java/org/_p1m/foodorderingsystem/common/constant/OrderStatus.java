package org._p1m.foodorderingsystem.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus implements BaseEnum<Integer>{
    PENDING(1,"Order has been placed and is waiting for restaurant approval."),
    APPROVED(2, "Restaurant has confirmed the order."),
    PREPARING(3, "Restaurant is preparing the food."),
    READY_FOR_PICKUP(4, "Order is ready for the delivery person to pick up."),
    PICKED_UP(5, "Delivery person has picked up the order."),
    DELIVERED(6, "Order has been delivered to the customer."),
    CANCELLED(7, "Order has been cancelled."),
    REJECTED(8, "Restaurant has rejected the order.");

    private final int value;
    private final String description;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
