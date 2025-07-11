package org._p1m.foodorderingsystem.features.superadmin_manage_user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.SuperAdminDashBoardResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.service.SuperAdminService;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/super_admin")
@Tag(name = "User API", description = "Endpoints for managing users")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @DeleteMapping("/user/delete/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Change user status to Inactive, .",
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
    public ResponseEntity<PaginatedApiResponse<SuperAdminDashBoardResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request
    ) {
        final PaginatedApiResponse<SuperAdminDashBoardResponse> response = superAdminService.getAllUsersPaginated(PageRequest.of(page, size));
//        System.out.println(userPage);
//        Map<String, Object> meta = new HashMap<>();
//        meta.put("pagination", new PaginationMeta(
//                userPage.getNumberOfElements(),
//                page * size + 1,
//                page * size + userPage.getNumberOfElements(),
//                userPage.getTotalElements()
//        ));
        return ResponseUtils.buildPaginatedResponse (request, response);
    }

    @GetMapping("/test")
    public String test() {
        return "Server is running!";
    }


}
