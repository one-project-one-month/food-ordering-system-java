package org._p1m.foodorderingsystem.features.processOrder.service;

import org._p1m.foodorderingsystem.features.processOrder.dto.request.UpdateOrderStatusRequestDTO;
import org._p1m.foodorderingsystem.features.processOrder.dto.response.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO request);
}
