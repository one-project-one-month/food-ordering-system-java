package org._p1m.foodorderingsystem.features.delivery.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org._p1m.foodorderingsystem.features.delivery.service.DeliveryDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
