package org._p1m.foodorderingsystem.features.users.controller;

import java.util.Map;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.users.dto.request.UploadProfilePictureRequest;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org._p1m.foodorderingsystem.features.users.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping("${api.base.path}/auth/users")
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

//	Get name
	@GetMapping
	public static String getUserName(){
		return
	}
//	get name end

    @PostMapping(
    	    value = "/{userId}/profile-picture",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	@Operation(
	    summary = "Upload profile picture",
	    description = "Uploads a profile picture for the specified user.",
	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	        description = "Multipart form with image file",
	        required = true,
	        content = @Content(
	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
	            schema = @Schema(implementation = UploadProfilePictureRequest.class)
	        )
	    ),
	    responses = {
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to upload file")
	    }
	)
	public ResponseEntity<?> uploadProfilePicture(
	    @Parameter(description = "User ID", required = true)
	    @PathVariable("userId") final Long userId,

	    @Parameter(hidden = true)
	    @RequestParam("file") final MultipartFile file
	) {
	    try {
	        final String fileUrl = this.userService.uploadProfilePicture(userId, file);
	        return ResponseEntity.ok(Map.of("url", fileUrl));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
	    }
	}
}
