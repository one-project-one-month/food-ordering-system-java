package org._p1m.foodorderingsystem.features.order.service;

import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.order.dto.request.OrderRequestDto;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
        PaginatedApiResponse<OrderResponseDto> getAllOrders(Pageable pageable);
        Long createOrder(OrderRequestDto dto);
        void updateOrder(Long id, OrderRequestDto dto);
        OrderResponseDto getOrder(Long id);
        String getDeliveryStatus(Long id); 
        
}

