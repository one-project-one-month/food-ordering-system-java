package org._p1m.foodorderingsystem.features.restaurant_registration.controller;

import jakarta.servlet.http.HttpServletRequest;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.request.RestaurantCreateRequest;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.request.RestaurantUpdateRequest;
import org._p1m.foodorderingsystem.features.restaurant_registration.srevice.RestaurantRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/auth/restaurants")
public class RestaurantRegistrationController {

    private final RestaurantRegistrationService restaurantRegistrationService;

    @Autowired
    public RestaurantRegistrationController(final RestaurantRegistrationService restaurantRegistrationService){
        this.restaurantRegistrationService = restaurantRegistrationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createRestaurant(@RequestBody RestaurantCreateRequest restaurantRequest,final HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.createRestaurant(restaurantRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> restaurantDetail(@PathVariable Long id,HttpServletRequest request){
        final ApiResponse response = this.restaurantRegistrationService.restaurantDetail(id);
        return  ResponseUtils.buildResponse(request,response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable Long id,HttpServletRequest request){
        final ApiResponse response = this.restaurantRegistrationService.deleteRestaurant(id);
        return  ResponseUtils.buildResponse(request,response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateRestaurant(@RequestBody RestaurantUpdateRequest restaurantUpdateRequest,
                                                        @PathVariable Long id,
                                                        HttpServletRequest request){
        final ApiResponse response = this.restaurantRegistrationService.updateRestaurant(id,restaurantUpdateRequest);
        return  ResponseUtils.buildResponse(request,response);

    }
}
