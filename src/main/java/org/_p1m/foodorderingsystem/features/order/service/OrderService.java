package org._p1m.foodorderingsystem.features.order.service;

import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDTO;
import org._p1m.foodorderingsystem.features.order.dto.resquest.OrderRequestDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO processOrder(OrderRequestDTO orderRequestDTO);
}
