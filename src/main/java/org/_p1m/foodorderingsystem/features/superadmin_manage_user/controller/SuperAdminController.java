package org._p1m.foodorderingsystem.features.superadmin_manage_user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.service.SuperAdminService;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/super_admin")
@Tag(name = "User API", description = "Endpoints for managing users")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @DeleteMapping("/user/delete/{id}")
    @Operation(
            summary = "Delete a  user",
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
}
