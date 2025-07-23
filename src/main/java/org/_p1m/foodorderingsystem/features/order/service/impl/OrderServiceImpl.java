package org._p1m.foodorderingsystem.features.order.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginationMeta;
import org._p1m.foodorderingsystem.features.menu.dto.responses.MenuResponseDto;
import org._p1m.foodorderingsystem.features.order.dto.request.OrderRequestDto;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.order.repository.OrderRepo;
import org._p1m.foodorderingsystem.features.order.repository.PaymentRepo;
import org._p1m.foodorderingsystem.features.order.service.OrderService;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.PaymentData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;

    @Override
    public PaginatedApiResponse<OrderResponseDto> getAllOrders(Pageable pageable) {
        Page<OrderData> page = orderRepo.findAll(pageable);

        List<OrderResponseDto> data = page.getContent().stream()
                .map(order -> {
                    OrderResponseDto dto = new OrderResponseDto();
                    dto.setId(order.getId());
                    dto.setOrderDateTime(order.getOrderDateTime());
                    dto.setUserAddress(order.getUserAddress());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setDeliveryStatus(order.getDeliveryStatus());
                    dto.setPaymentId(order.getPayment().getId());
                    dto.setCreatedAt(order.getCreatedAt());
                    return dto;
                })
                .toList();
        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(page.getTotalElements());
        meta.setTotalPages(page.getTotalPages());
        meta.setCurrentPage(pageable.getPageNumber()+1);
        return PaginatedApiResponse.<OrderResponseDto>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Fetched successfully")
                .meta(meta)
                .data(data)
                .build();
    }


    @Override
    public Long createOrder(OrderRequestDto dto) {
        OrderData order = new OrderData();
        order.setOrderDateTime(LocalDateTime.now());
        order.setUserAddress(dto.getUserAddress());
        order.setTotalAmount(dto.getTotalAmount());
        order.setDeliveryStatus(DeliveryStatus.PENDING);

        PaymentData payment = paymentRepo.findById(dto.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        order.setPayment(payment);

        OrderData saved = orderRepo.save(order);
        return saved.getId();
    }

    @Override
    public void updateOrder(Long id, OrderRequestDto dto) {
        OrderData order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderDateTime(dto.getOrderDateTime());
        order.setUserAddress(dto.getUserAddress());
        order.setTotalAmount(dto.getTotalAmount());

        PaymentData payment = paymentRepo.findById(dto.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        order.setPayment(payment);

        orderRepo.save(order);
    }

    @Override
    public OrderResponseDto getOrder(Long id) {
        OrderData order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDateTime(order.getOrderDateTime());
        dto.setUserAddress(order.getUserAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDeliveryStatus(order.getDeliveryStatus());
        dto.setPaymentId(order.getPayment().getId());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    @Override
    public String getDeliveryStatus(Long id) {
        return orderRepo.findById(id)
                .map(order -> order.getDeliveryStatus().name())
                .orElse("NOT_FOUND");
    }
}

