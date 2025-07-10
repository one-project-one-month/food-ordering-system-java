package org._p1m.foodorderingsystem.features.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.RestaurantCreateRequest;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.RestaurantUpdateRequest;
import org._p1m.foodorderingsystem.features.restaurant.srevice.RestaurantRegistrationService;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/auth/restaurants")
@Tag(name = "Restaurant API", description = "Endpoints for managing restaurants")
public class RestaurantRegistrationController {

    private final RestaurantRegistrationService restaurantRegistrationService;

    @Autowired
    public RestaurantRegistrationController(final RestaurantRegistrationService restaurantRegistrationService) {
        this.restaurantRegistrationService = restaurantRegistrationService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new restaurant",
            description = "Registers a new restaurant in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Restaurant creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RestaurantCreateRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> createRestaurant(@Valid @RequestBody RestaurantCreateRequest restaurantRequest, final HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.createRestaurant(restaurantRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get restaurant detail",
            description = "Get Restaurant detail by its ID.",
            parameters = {
                    @Parameter(name = "id", description = "Restaurant ID", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant details retrieved successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<ApiResponse> restaurantDetail(@PathVariable Long id, HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.restaurantDetail(id);
        return ResponseUtils.buildResponse(request, response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a restaurant",
            description = "Deletes a restaurant from the system by its ID.",
            parameters = {
                    @Parameter(name = "id", description = "Restaurant ID", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant deleted successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable Long id, HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.deleteRestaurant(id);
        return ResponseUtils.buildResponse(request, response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a restaurant",
            description = "Updates the details of an existing restaurant.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Restaurant update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RestaurantUpdateRequest.class))
            ),
            parameters = {
                    @Parameter(name = "id", description = "Restaurant ID", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<ApiResponse> updateRestaurant(@Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest,
                                                        @PathVariable Long id,
                                                        HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.updateRestaurant(id, restaurantUpdateRequest);
        return ResponseUtils.buildResponse(request, response);

    }
}
