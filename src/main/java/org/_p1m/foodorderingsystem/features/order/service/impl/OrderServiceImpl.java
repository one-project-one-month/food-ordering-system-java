package org._p1m.foodorderingsystem.features.order.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.OrderStatus;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginationMeta;
import org._p1m.foodorderingsystem.features.addCart.repository.AddCartMenuRepo;
import org._p1m.foodorderingsystem.features.address.repository.AddressRepository;
import org._p1m.foodorderingsystem.features.order.dto.request.OrderRequest;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.order.repository.OrderRepo;
import org._p1m.foodorderingsystem.features.order.repository.PaymentRepo;
import org._p1m.foodorderingsystem.features.order.service.OrderService;
import org._p1m.foodorderingsystem.model.AddCartData;
import org._p1m.foodorderingsystem.model.Address;
import org._p1m.foodorderingsystem.model.OrderData;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final AddressRepository addressRepo;
    private final AddCartMenuRepo addCartMenuRepo;
    private final ModelMapper modelMapper;
    
    @Override
    public PaginatedApiResponse<OrderResponseDto> getAllOrders(Pageable pageable, Long restaurantId, DeliveryStatus status) {
        Page<OrderData> page = orderRepo.findOrdersByRestaurantIdWithStatus(restaurantId, pageable, status);
        return buildPaginatedResponse(page, pageable);
    }

    @Override
    public ApiResponse createOrder(OrderRequest dto) {
    	Address address = addressRepo.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        OrderData order = new OrderData();
        order.setOrderDateTime(LocalDateTime.now());
        order.setUserAddress(address);
        order.setTotalAmount(dto.getTotalAmount());
        order.setDeliveryStatus(DeliveryStatus.PENDING);
//        PaymentData payment = paymentRepo.findById(dto.getPaymentId())
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//        order.setPayment(payment);
        List<AddCartData> items = addCartMenuRepo
        		.findUnorderedCartItemsByCustomerId(dto.getCustomerId());
        for (AddCartData item : items) {
            order.addCartItem(item);
        }
        OrderData saved = orderRepo.save(order);
        OrderResponseDto response = modelMapper.map(saved, OrderResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(response)
                .message("Order created successfully").build();
    }

    @Override
    public void updateOrder(Long id, OrderRequest dto) {
        OrderData order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Address address = addressRepo.findById(dto.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));
        order.setOrderDateTime(LocalDateTime.now());
        order.setUserAddress(address);
//        order.setTotalAmount(new BigDecimal(dto.getTotalAmount()));

//        PaymentData payment = paymentRepo.findById(dto.getPaymentId())
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//        order.setPayment(payment);

        orderRepo.save(order);
    }

    @Override
    public OrderResponseDto getOrder(Long id) {
        OrderData order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDateTime(order.getOrderDateTime());
        dto.setAddressId(order.getUserAddress().getId());
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


    @Override
    public PaginatedApiResponse<OrderResponseDto> getAllOrdersWithStatus(Pageable pageable, Long customerId, DeliveryStatus status) {
        Page<OrderData> page = orderRepo.findOrdersByCustomerIdWithStatus(customerId, pageable, status);
        return buildPaginatedResponse(page, pageable);
    }

	
	private OrderResponseDto convertToDto(OrderData order) {
	    OrderResponseDto dto = new OrderResponseDto();
	    dto.setId(order.getId());
	    dto.setOrderDateTime(order.getOrderDateTime());
	    dto.setAddressId(order.getUserAddress() != null ? order.getUserAddress().getId() : null);
	    dto.setTotalAmount(order.getTotalAmount());
	    dto.setDeliveryStatus(order.getDeliveryStatus());
	    dto.setPaymentId(order.getPayment() != null ? order.getPayment().getId() : null);
	    dto.setCreatedAt(order.getCreatedAt());
	    return dto;
	}
	
	private PaginatedApiResponse<OrderResponseDto> buildPaginatedResponse(Page<OrderData> page, Pageable pageable) {
	    List<OrderResponseDto> data = page.getContent().stream()
	        .map(this::convertToDto)
	        .toList();

	    PaginationMeta meta = new PaginationMeta();
	    meta.setTotalItems(page.getTotalElements());
	    meta.setTotalPages(page.getTotalPages());
	    meta.setCurrentPage(pageable.getPageNumber() + 1);

	    return PaginatedApiResponse.<OrderResponseDto>builder()
	            .success(1)
	            .code(HttpStatus.OK.value())
	            .message("Fetched successfully")
	            .meta(meta)
	            .data(data)
	            .build();
	}


}

