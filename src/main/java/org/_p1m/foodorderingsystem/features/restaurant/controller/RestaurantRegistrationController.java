package org._p1m.foodorderingsystem.features.restaurant.controller;

import java.util.Map;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.RestaurantCreateRequest;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.RestaurantUpdateRequest;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.UploadRestaurantImgRequest;
import org._p1m.foodorderingsystem.features.restaurant.service.RestaurantRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.base.path}/restaurants")
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

    @PostMapping(
            value = "/{restaurantId}/restaurant-img",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Upload  picture for restaurant",
            description = "Uploads a  picture for restaurant with its id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Multipart form with image file",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = UploadRestaurantImgRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to upload file")
            }
    )
    public ResponseEntity<?> uploadRestaurantPicture(
            @Parameter(description = "Restaurant ID", required = true)
            @PathVariable("restaurantId") final Long restaurantId,

            @Parameter(hidden = true)
            @RequestParam("file") final MultipartFile file
    ) {
        try {
            final String fileUrl = this.restaurantRegistrationService.uploadRestaurantPicture(restaurantId, file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get restaurant detail",
            description = "Get Restaurant detail by its ID.",
            parameters = {
                    @Parameter(name = "id",description = "Restaurant ID", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant details retrieved successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<ApiResponse> restaurantDetail(
    @PathVariable(name = "id") Long id, HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.restaurantDetail(id);
        return ResponseUtils.buildResponse(request, response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a restaurant",
            description = "Deletes a restaurant from the system by its ID.",
            parameters = {
                    @Parameter(name = "id",description = "Restaurant ID", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant deleted successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.deleteRestaurant(id);
        return ResponseUtils.buildResponse(request, response);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Update a restaurant",
            description = "Updates the details of an existing restaurant.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Restaurant update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RestaurantUpdateRequest.class))
            ),
    		parameters = {
                    @Parameter(name = "id",description = "Restaurant ID", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Restaurant not found")
            }
    )
    public ResponseEntity<ApiResponse> updateRestaurant(
    		@Valid @RequestBody RestaurantUpdateRequest restaurantUpdateRequest,
    		 @PathVariable(name = "id") Long id, HttpServletRequest request) {
        final ApiResponse response = this.restaurantRegistrationService.updateRestaurant(id, restaurantUpdateRequest);
        return ResponseUtils.buildResponse(request, response);

    }
}
