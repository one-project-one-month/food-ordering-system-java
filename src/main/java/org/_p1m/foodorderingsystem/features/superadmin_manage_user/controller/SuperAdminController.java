package org._p1m.foodorderingsystem.features.superadmin_manage_user.controller;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.SuperAdminDashBoardResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.service.SuperAdminService;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/super_admin")
@Tag(name = "User API", description = "Endpoints for managing users")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @DeleteMapping("/user/delete/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Change user status to Inactive.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User deleting request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRequest.class))
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User is successfully deleted"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid user")
            }
    )
    public ResponseEntity<ApiResponse> deletedUser(
            @PathVariable final Long id, HttpServletRequest request){

        final ApiResponse response = superAdminService.deleteById(id);
        return ResponseUtils.buildResponse(request, response);
    }


    @GetMapping("/all-users")
    @Operation(
            summary = "Fetching Users",
            description = "Fetching Users with keywords - name,email,phone,address,role and status, returning pagination",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users are fetched successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Invalid Request")
            }
    )
    public ResponseEntity<PaginatedApiResponse<SuperAdminDashBoardResponse>> getAllUsers(
    		@Parameter(description = "Search keyword") 
    		@RequestParam(value = "keyword", required = false) String keyword,
    		@Parameter(description = "User role to filter") 
    		@RequestParam(value = "role", required = false) String role,
    		@Parameter(description = "User status to filter") 
    		@RequestParam(value = "status", required = false) String status,
    		@Parameter(description = "Page number (starts from 0)") 
    		@RequestParam(value = "page", defaultValue = "0") int page,
    		@Parameter(description = "Page size") 
    		@RequestParam(value = "size", defaultValue = "20") int size,
            HttpServletRequest request
    ) {
        final Pageable pageable = PageRequest.of(page, size);
        final PaginatedApiResponse<SuperAdminDashBoardResponse> response = superAdminService.getAllUsersPaginated(keyword,role,status,pageable);
//        System.out.println(userPage);
//        Map<String, Object> meta = new HashMap<>();
//        meta.put("pagination", new PaginationMeta(
//                userPage.getNumberOfElements(),
//                page * size + 1,
//                page * size + userPage.getNumberOfElements(),
//                userPage.getTotalElements()
//        ));
        return ResponseUtils.buildPaginatedResponse(request, response);
    }

    @GetMapping("/test")
    public String test() {
        return "Server is running!";
    }


}
