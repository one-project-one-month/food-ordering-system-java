package org._p1m.foodorderingsystem.features.processOrder.controller;

import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.ProcessOrderRequest;
import org._p1m.foodorderingsystem.features.processOrder.dto.request.UpdateOrderStatusRequestDTO;
import org._p1m.foodorderingsystem.features.processOrder.dto.response.OrderResponseDTO;
import org._p1m.foodorderingsystem.features.processOrder.service.ProcessOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Order API", description = "Endpoints for managing orders")
@RequestMapping("/${api.base.path}/orders")
public class ProcessOrderController {
    private final ProcessOrderService orderService;

    public ProcessOrderController(ProcessOrderService orderService) {
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

