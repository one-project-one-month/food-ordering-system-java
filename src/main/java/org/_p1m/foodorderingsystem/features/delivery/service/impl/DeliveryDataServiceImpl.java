package org._p1m.foodorderingsystem.features.delivery.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.response.AssignDeliveryResponseDto;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllVendorsResponseDto;
import org._p1m.foodorderingsystem.features.delivery.service.DeliveryDataService;
import org._p1m.foodorderingsystem.features.order.repository.OrderDataRepository;
import org._p1m.foodorderingsystem.features.restaurant_vendors.repository.RestaurantVendorRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryDataServiceImpl implements DeliveryDataService {

    private final OrderDataRepository orderDataRepository;
    private final UserRepository userRepository;
    private final RestaurantVendorRepository restaurantVendorRepository;

    @Override
    @Transactional
    public ApiResponse assignDelivery(AssignDeliveryRequest assignDeliveryRequest) {

        OrderData orderData = this.orderDataRepository.findById(assignDeliveryRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        User deliveryStaff = this.userRepository.findById(assignDeliveryRequest.getDeliveryId())
                .orElseThrow(() -> new EntityNotFoundException("Delivery Staff not found"));

        RestaurantVendor restaurantVendor = this.restaurantVendorRepository.findByRestaurantIdAndDeliveryUserId(assignDeliveryRequest.getRestaurantId(),
                        assignDeliveryRequest.getDeliveryId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant Vendor not found"));

        if (restaurantVendor.getStatus() == Status.INACTIVE) {
            throw new EntityNotFoundException("Your assign delivery staff is not free.");
        }

        orderData.setDeliveryStatus(DeliveryStatus.ON_THE_WAY);
        this.orderDataRepository.save(orderData);

        this.restaurantVendorRepository.updateDeliveryStatus(assignDeliveryRequest.getRestaurantId(),
                assignDeliveryRequest.getDeliveryId(),
                Status.INACTIVE);


        AssignDeliveryResponseDto dto = new AssignDeliveryResponseDto(
                orderData.getId(),
                deliveryStaff.getId(),
                Status.INACTIVE
        );

        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("Delivery Data", dto))
                .message("Assign Delivery Successfully")
                .build();
    }

    @Override
    public ApiResponse getAllDeliveryStaffData(Long restaurantId) {
        List<RestaurantVendor> restaurantVendors = this.restaurantVendorRepository.findByRestaurantId(restaurantId);

        if(restaurantVendors.isEmpty()){
            return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                    .message("No delivery in this restaurant")
                    .build();
        }

    // if we want free delivery staffs
//        List<RestaurantVendor> activeVendors = restaurantVendors.stream()
//                .filter(v -> v.getStatus() == Status.ACTIVE)
//                .toList();
        List<GetAllVendorsResponseDto> dto = restaurantVendors.stream()
                .map(vendor -> new GetAllVendorsResponseDto(
                        vendor.getDeliveryUser().getId(),
                        vendor.getStatus()
                ))
                .toList();

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("Delivery Staff Data", dto))
                .message("Retrieve Delivery Staff Data Successfully")
                .build();
    }
}
