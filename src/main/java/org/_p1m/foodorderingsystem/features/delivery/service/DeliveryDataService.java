package org._p1m.foodorderingsystem.features.delivery.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.delivery.dto.request.ApplyDeliveryStaffRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryDataService {
    ApiResponse assignDelivery(AssignDeliveryRequest assignDeliveryRequest);

    ApiResponse getAllDeliveryStaffData(Long restaurantId);
    
    ApiResponse applyRestaurantByDeliveryStaff(ApplyDeliveryStaffRequest request);
}
