package org._p1m.foodorderingsystem.features.order.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.order.dto.request.OrderRequest;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
        PaginatedApiResponse<OrderResponseDto> getAllOrders(Pageable pageable);
        ApiResponse createOrder(OrderRequest dto);
        void updateOrder(Long id, OrderRequest dto);
        OrderResponseDto getOrder(Long id);
        String getDeliveryStatus(Long id); 
        
}

