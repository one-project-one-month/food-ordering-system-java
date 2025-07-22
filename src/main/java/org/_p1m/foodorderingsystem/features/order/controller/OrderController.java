package org._p1m.foodorderingsystem.features.order.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.order.dto.request.OrderRequestDto;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.order.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.base.path}/requestOrder")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all-orders")
    public ResponseEntity<PaginatedApiResponse<OrderResponseDto>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PaginatedApiResponse<OrderResponseDto> response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create-orders")
    public ResponseEntity<PaginatedApiResponse<Long>> createOrder(@RequestBody @Valid OrderRequestDto dto) {
        Long id = orderService.createOrder(dto);

        PaginatedApiResponse<Long> response = PaginatedApiResponse.<Long>builder()
                .success(1)
                .code(201)
                .message("Order created successfully")
                .meta(Map.of(
                        "additionalProp1", Map.of(),
                        "additionalProp2", Map.of(),
                        "additionalProp3", Map.of()
                ))
                .data(Collections.singletonList(id))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-order/{id}")
    public ResponseEntity<PaginatedApiResponse<Void>> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderRequestDto dto) {
        orderService.updateOrder(id, dto);

        PaginatedApiResponse<Void> response = PaginatedApiResponse.<Void>builder()
                .success(1)
                .code(200)
                .message("Order updated successfully")
                .meta(Map.of())
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-order/{id}")
    public ResponseEntity<PaginatedApiResponse<OrderResponseDto>> getOrder(@PathVariable Long id) {
        OrderResponseDto dto = orderService.getOrder(id);

        PaginatedApiResponse<OrderResponseDto> response = PaginatedApiResponse.<OrderResponseDto>builder()
                .success(1)
                .code(200)
                .message("Order fetched successfully")
                .meta(Map.of())
                .data((List<OrderResponseDto>) dto)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkDeliveryStatus/{id}")
    public ResponseEntity<PaginatedApiResponse<String>> checkDeliveryStatus(@PathVariable Long id) {
        String status = orderService.getDeliveryStatus(id);

        PaginatedApiResponse<String> response = PaginatedApiResponse.<String>builder()
                .success(1)
                .code(200)
                .message("Delivery status fetched successfully")
                .meta(Map.of())
                .data(Collections.singletonList(status))
                .build();

        return ResponseEntity.ok(response);
    }
}

