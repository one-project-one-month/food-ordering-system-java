package org._p1m.foodorderingsystem.features.users.controller;

import java.util.Map;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org._p1m.foodorderingsystem.features.users.service.UserService;
import org.springframework.http.ResponseEntity;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/users")
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Registers a new user in the system.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User creation request",
            required = true,
            content = @Content(schema = @Schema(implementation = UserCreateRequest.class))
        ),
        responses = {
        		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User created successfully"),
        		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
        }
    )
    public ResponseEntity<ApiResponse> createUser(
        @RequestBody final UserCreateRequest userRequest,
        final HttpServletRequest request
    ) {
        final ApiResponse response = this.userService.createUser(userRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @PostMapping("/{userId}/profile-picture")
    @Operation(
        summary = "Upload profile picture",
        description = "Uploads a profile picture for the specified user.",
        responses = {
        	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to upload file")
        }
    )
    public ResponseEntity<?> uploadProfilePicture(
        @Parameter(description = "User ID") @PathVariable final Long userId,
        @Parameter(description = "Image file") @RequestParam("file") final MultipartFile file
    ) {
        try {
            final String fileUrl = this.userService.uploadProfilePicture(userId, file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
