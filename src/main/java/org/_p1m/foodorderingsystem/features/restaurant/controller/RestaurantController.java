package org._p1m.foodorderingsystem.features.restaurant.controller;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.restaurant.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.path}/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant API", description = "Endpoints for managing restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRestaurants(HttpServletRequest request) {
        ApiResponse response = restaurantService.getAllRestaurants();
        return ResponseUtils.buildResponse(request, response);
    }
    
    @GetMapping("/users/{userId}")
    @Operation(
            summary = "Get a restaurant by user ID",
            description = "Retrieves a restaurant by its ID.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of user to retrieve", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant retrieve by user id successfully"),
            }
    )
    public ResponseEntity<ApiResponse> getRestaurantByUserId(@PathVariable(name="userId") Long userId, HttpServletRequest request){
    	ApiResponse response = restaurantService.getRestaurantByUserId(userId);
    	return ResponseUtils.buildResponse(request, response);
    }
}



