package org._p1m.foodorderingsystem.features.delivery.controller;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.delivery.dto.request.ApplyDeliveryStaffRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.request.AssignDeliveryRequest;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllAssignedDelivery;
import org._p1m.foodorderingsystem.features.delivery.dto.response.GetAllVendorsResponseDto;
import org._p1m.foodorderingsystem.features.delivery.service.DeliveryDataService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
   @Operation(
           summary = "Fetching deliveries",
           description = "Fetching deliveries with pagination",
           responses = {
                   @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Deliveries are fetched successfully"),
                   @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
           }
   )
   public ResponseEntity<PaginatedApiResponse<GetAllVendorsResponseDto>> getAllDeliveryStaffData(
		@Parameter(description = "Page number") 
   		@RequestParam(value = "page", defaultValue = "0") int page,
   		@Parameter(description = "Page size") 
   		@RequestParam(value = "size", defaultValue = "20") int size,
   		@RequestParam(value = "status", defaultValue = "ACTIVE") Status status,
		@PathVariable(name="restaurantId") final Long restaurantId, HttpServletRequest request){
	   Pageable pageable = PageRequest.of(page, size);
       final PaginatedApiResponse<GetAllVendorsResponseDto> response = this.deliveryDataService.getAllDeliveryStaffData(pageable,restaurantId,status);
       return ResponseEntity.ok(response);
   }

   @PostMapping("/applyDelivery")
   public ResponseEntity<ApiResponse> applyRestaurantByDeliveryStaff(@Valid @RequestBody ApplyDeliveryStaffRequest staffRequest, HttpServletRequest request){
       final ApiResponse response = this.deliveryDataService.applyRestaurantByDeliveryStaff(staffRequest);
       return ResponseUtils.buildResponse(request, response);
   }
   
   @GetMapping("/assignment/{deliveryId}")
   @Operation(
           summary = "Fetching assigned deliveries",
           description = "Fetching assigned deliveries with pagination",
           responses = {
                   @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Assigned deliveries fetched successfully"),
                   @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
           }
   )
   public ResponseEntity<PaginatedApiResponse<GetAllAssignedDelivery>> getAllAssignedDelivery(
		@Parameter(description = "Page number") 
   		@RequestParam(value = "page", defaultValue = "0") int page,
   		@Parameter(description = "Page size")
   		@RequestParam(value = "size", defaultValue = "20") int size,
   		@RequestParam(value = "status", defaultValue = "ACCEPTED") DeliveryStatus status,
		@PathVariable(name="deliveryId") final Long deliveryId, HttpServletRequest request){
	   Pageable pageable = PageRequest.of(page, size);
       final PaginatedApiResponse<GetAllAssignedDelivery> response = this.deliveryDataService.getAllAssignedDelivery(pageable,deliveryId,status);
       return ResponseUtils.buildPaginatedResponse(request, response);
   }
}
