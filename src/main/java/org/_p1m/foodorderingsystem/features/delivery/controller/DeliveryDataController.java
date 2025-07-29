package org._p1m.foodorderingsystem.features.delivery.controller;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.delivery.dto.request.ApplyDeliveryStaffRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org._p1m.foodorderingsystem.features.delivery.service.DeliveryDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.base.path}/delivery-data")
@Tag(name = "Delivery Assign API", description = "Endpoints for Assign delivery")
public class DeliveryDataController {

    private final DeliveryDataService deliveryDataService;

    DeliveryDataController(DeliveryDataService deliveryDataService){
        this.deliveryDataService = deliveryDataService;
    }

   @PatchMapping("/assignDelivery")
    public ResponseEntity<ApiResponse> assignDelivery(@Valid @RequestBody AssignDeliveryRequest assignDeliveryRequest, HttpServletRequest request){
       final ApiResponse response = this.deliveryDataService.assignDelivery(assignDeliveryRequest);
       return ResponseUtils.buildResponse(request, response);
   }

   @GetMapping("/{restaurantId}")
   public ResponseEntity<ApiResponse> getAllDeliveryStaffData(@PathVariable final Long restaurantId, HttpServletRequest request){
       final ApiResponse response = this.deliveryDataService.getAllDeliveryStaffData(restaurantId);
       return ResponseUtils.buildResponse(request, response);
   }

   @PostMapping("/applyDelivery")
   public ResponseEntity<ApiResponse> applyRestaurantByDeliveryStaff(@Valid @RequestBody ApplyDeliveryStaffRequest staffRequest, HttpServletRequest request){
       final ApiResponse response = this.deliveryDataService.applyRestaurantByDeliveryStaff(staffRequest);
       return ResponseUtils.buildResponse(request, response);
   }
}
