package org._p1m.foodorderingsystem.features.menu.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.service.ManageMenuService;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
