package org._p1m.foodorderingsystem.features.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.menu.dto.request.DishSizeRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.ExtraRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateDishSizeRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UploadMenuImageRequest;
import org._p1m.foodorderingsystem.features.menu.service.DishSizeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("${api.base.path}/dish-sizes")
@Tag(name = "Dish Size API", description = "Endpoints for managing Dish Size")
public class DishSizeController {

    private final DishSizeService dishSizeService;

    public DishSizeController(DishSizeService dishSizeService){
        this.dishSizeService = dishSizeService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new Dish Size for specific menu",
            description = "create a new dish size for menu",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body with new dish size data",
                    required = true,
            		content = @Content(
            			    schema = @Schema(implementation = DishSizeRequest.class),
            			    examples = @ExampleObject(value = """
            			    {
            			      "name": "string",
            			      "price": 0,
            			      "menuId": 10
            			    }
            			    """)
            			  )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Dish Size created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> createDishSize(@Valid @RequestBody  DishSizeRequest dishSizeRequest, HttpServletRequest request){
        final ApiResponse response = this.dishSizeService.createDishSize(dishSizeRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @PostMapping(
    		value="/{dishSizeId}/dishSize-img",
    		consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload DishSize image",
            description = "Uploads dish size image for the specified menu.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Multipart form with image file",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = UploadMenuImageRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to upload file")
            }
    )
    public ResponseEntity<?> uploadDishSizeImage(
            @Parameter(description = "Dish Size ID") @PathVariable(name="dishSizeId") final Long dishSizeId,
            @Parameter(description = "Image file") @RequestParam("file") final MultipartFile file
    ) {
        try {
            final String fileUrl = this.dishSizeService.uploadDishSizeImage(dishSizeId, file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @PatchMapping("/{dishSizeId}")
    @Operation(
            summary = "Update Dish Size for specific menu",
            description = "Update dish size for menu",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body with update dish size data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateDishSizeRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dish Size created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> updateDishSize(@Valid @RequestBody  UpdateDishSizeRequest updateDishSizeRequest,
    		@PathVariable(name="dishSizeId") final Long dishSizeId,
                                                      HttpServletRequest request){
        final ApiResponse response = this.dishSizeService.updateDishSize(dishSizeId,updateDishSizeRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @DeleteMapping("/{dishSizeId}")
    @Operation(
            summary = "Delete Dish Size",
            description = "Deletes Dish Size by its ID.",
            parameters = {
                    @Parameter(name = "dishSizeId", description = "Dish Size Id  to delete", required = true)
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Dish size delete successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Extra not found")
            }
    )
    public ResponseEntity<ApiResponse> deleteDishSize(@PathVariable(name="dishSizeId") Long dishSizeId, HttpServletRequest request) {
        final ApiResponse response = this.dishSizeService.deleteMenu(dishSizeId);
        return ResponseUtils.buildResponse(request, response);
    }

}
