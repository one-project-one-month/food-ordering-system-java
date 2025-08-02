package org._p1m.foodorderingsystem.features.delivery.service;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.delivery.dto.request.ApplyDeliveryStaffRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.request.ChangeDeliveryStatusRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllAssignedDelivery;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllVendorsResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryDataService {
    ApiResponse assignDelivery(AssignDeliveryRequest assignDeliveryRequest);

    PaginatedApiResponse<GetAllVendorsResponseDto>  getAllDeliveryStaffData(Pageable pageable,Long restaurantId,Status status);
    
    ApiResponse applyRestaurantByDeliveryStaff(ApplyDeliveryStaffRequest request);
    
    PaginatedApiResponse<GetAllAssignedDelivery> getAllAssignedDelivery(Pageable pageable,Long restaurantId,DeliveryStatus status);
    
    ApiResponse changeDeliveryStatus(ChangeDeliveryStatusRequest changeDeliStatusReq);
}
