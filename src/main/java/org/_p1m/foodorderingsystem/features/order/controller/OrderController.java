package org._p1m.foodorderingsystem.features.order.controller;

import java.util.Collections;
import java.util.List;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllAssignedDelivery;
import org._p1m.foodorderingsystem.features.order.dto.request.OrderRequest;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.order.service.OrderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.path}/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all-orders/{restaurantId}")
    @Operation(
            summary = "Fetching Orders",
            description = "Fetching orders with pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users are fetched successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
            }
    )
    public ResponseEntity<PaginatedApiResponse<OrderResponseDto>> getAllOrders(
    		@Parameter(description = "Page number") 
    		@RequestParam(value = "page", defaultValue = "0") int page,
    		@Parameter(description = "Page size") 
    		@RequestParam(value = "size", defaultValue = "20") int size,
    		@RequestParam(value = "status", defaultValue = "PENDING") DeliveryStatus status,
    		@PathVariable(name="restaurantId") final Long restaurantId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PaginatedApiResponse<OrderResponseDto> response = orderService.getAllOrders(pageable,restaurantId,status);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(
            summary = "Create an order",
            description = "Create an order with request body before payment.",
            		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Order creation request",
                            required = true,
                            content = @Content(schema = @Schema(implementation = OrderRequest.class))
                    ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order created successfully"),
            }
    )
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest odRequest, HttpServletRequest request) {
        ApiResponse response = orderService.createOrder(odRequest);

//        PaginatedApiResponse<Long> response = PaginatedApiResponse.<Long>builder()
//                .success(1)
//                .code(201)
//                .message("Order created successfully")
//                .meta(null)
//                .data(Collections.singletonList(id))
//                .build();

        return ResponseUtils.buildResponse(request, response);
    }

    @PutMapping("/update-order/{id}")
    public ResponseEntity<PaginatedApiResponse<Void>> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderRequest dto) {
        orderService.updateOrder(id, dto);

        PaginatedApiResponse<Void> response = PaginatedApiResponse.<Void>builder()
                .success(1)
                .code(200)
                .message("Order updated successfully")
                .meta(null)
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
                .meta(null)
                .data(List.of(dto))
                .build();

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{customerId}")
    @Operation(
            summary = "Fetch customer orders by delivery status",
            description = "Retrieves a paginated list of orders for a specific customer filtered by delivery status.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Fetched successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
            }
    )
    public ResponseEntity<PaginatedApiResponse<OrderResponseDto>> getAllAssignedDelivery(
 		@Parameter(description = "Page number") 
    		@RequestParam(value = "page", defaultValue = "0") int page,
    		@Parameter(description = "Page size")
    		@RequestParam(value = "size", defaultValue = "20") int size,
    		@RequestParam(value = "status", defaultValue = "PENDING") DeliveryStatus status,
 		@PathVariable(name="customerId") final Long customerId, HttpServletRequest request){
 	   Pageable pageable = PageRequest.of(page, size);
        final PaginatedApiResponse<OrderResponseDto> response = this.orderService.getAllOrdersWithStatus(pageable,customerId,status);
        return ResponseUtils.buildPaginatedResponse(request, response);
    }

    @GetMapping("/checkDeliveryStatus/{id}")
    public ResponseEntity<PaginatedApiResponse<String>> checkDeliveryStatus(@PathVariable Long id) {
        String status = orderService.getDeliveryStatus(id);

        PaginatedApiResponse<String> response = PaginatedApiResponse.<String>builder()
                .success(1)
                .code(200)
                .message("Delivery status fetched successfully")
                .meta(null)
                .data(Collections.singletonList(status))
                .build();

        return ResponseEntity.ok(response);
    }
}

