package org._p1m.foodorderingsystem.features.menu.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.service.ManageMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("${api.base.path}/auth/menus")
@Tag(name = "Menu API", description = "Endpoints for managing menus")
public class ManageMenuController {

    private  final ManageMenuService manageMenuService;

    ManageMenuController(ManageMenuService manageMenuService){
        this.manageMenuService = manageMenuService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new menu",
            description = "create a new menu for restaurant",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Create Menu request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateMenuRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Menu created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<ApiResponse> createMenu(@Valid @RequestBody CreateMenuRequest createMenuRequest, HttpServletRequest request){
        final ApiResponse response = this.manageMenuService.createMenu(createMenuRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @PostMapping("/{menuId}/menu-img")
    @Operation(
            summary = "Upload Menu image",
            description = "Uploads a menu picture for the specified menu.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to upload file")
            }
    )
    public ResponseEntity<?> uploadMenuImage(
            @Parameter(description = "Menu ID") @PathVariable final Long menuId,
            @Parameter(description = "Image file") @RequestParam("file") final MultipartFile file
    ) {
        try {
            final String fileUrl = this.manageMenuService.uploadMenuImage(menuId, file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
