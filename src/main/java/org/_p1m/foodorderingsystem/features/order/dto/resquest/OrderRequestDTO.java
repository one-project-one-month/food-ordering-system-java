package org._p1m.foodorderingsystem.features.order.dto.resquest;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private Long customerId;
    private Long restaurantId;
    private String deliveryAddress;
    private List<OrderItemDTO> items;
    private String couponCode;

    private Long orderId;
    private String action;

    @Getter
    @Setter
    class OrderItemDTO {
        private Long menuId;
        private int quantity;
        private List<Long> extraIds;
    }
}
