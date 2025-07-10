package org._p1m.foodorderingsystem.features.users.controller;

import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.users.dto.request.DashboardRequestDto;
import org._p1m.foodorderingsystem.features.users.dto.response.AdminDashboardResponseDto;
import org._p1m.foodorderingsystem.features.users.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/admin")
@Tag(name = "Admin dashboard API", description = "Endpoints for admin dashboard")
public class AdminDashboardController {
	private final DashboardService dashboardService;
	
	@PostMapping("/dashboard")
    @Operation(
        summary = "Get all users for admin dashboard with pagination",
        description = "Get all users from the system.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Admin dashboard request",
            required = true,
            content = @Content(schema = @Schema(implementation = DashboardRequestDto.class))
        ),
        responses = {
        		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User data for admin dashboard retrieved successfully."),
        		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
        }
    )
    public ResponseEntity<PaginatedApiResponse<AdminDashboardResponseDto>> getAdminDashboard(
    		final HttpServletRequest request,
//            @RequestHeader(value = "Authorization") final String authHeader,
            @Validated @RequestBody final DashboardRequestDto dashboardRequestDto
    ) {
        PaginatedApiResponse<AdminDashboardResponseDto> response = this.dashboardService.getAdminDashboard(request,""/*authHeader here*/,dashboardRequestDto);
        return ResponseUtils.buildPaginatedResponse(request, response);
    }
}
