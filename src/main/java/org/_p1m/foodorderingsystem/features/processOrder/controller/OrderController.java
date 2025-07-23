package org._p1m.foodorderingsystem.features.processOrder.controller;

import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.ProcessOrderRequest;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.UpdateOrderStatusRequestDTO;
import org._p1m.foodorderingsystem.features.processOrder.dto.response.OrderResponseDTO;
import org._p1m.foodorderingsystem.features.processOrder.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/processOrder")
    public ResponseEntity<ApiResponse> processOrder(@Valid @RequestBody ProcessOrderRequest request) {
        OrderResponseDTO responseData = orderService.processOrder(request);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Order processed successfully.")
                .data(responseData)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PatchMapping("/{orderId}/process")
    public ResponseEntity<ApiResponse> processOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDTO request) {
        OrderResponseDTO responseData = orderService.updateOrderStatus(orderId, request);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Order Status updated successfully.")
                .data(responseData)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}

