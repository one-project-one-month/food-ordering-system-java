package org._p1m.foodorderingsystem.features.profile.controller;


import com.cloudinary.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.profile.dto.request.ProfileRequestDto;
import org._p1m.foodorderingsystem.features.profile.dto.response.ProfileResponseDto;
import org._p1m.foodorderingsystem.features.profile.util.ApiErrorResponse;
import org._p1m.foodorderingsystem.features.users.dto.request.UploadProfilePictureRequest;
import org._p1m.foodorderingsystem.features.users.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/auth/profile")
@Tag(name = "Profile API", description = "Endpoints for managing profile")
public class ProfileController {

    private final ProfileService profileService;



    @PostMapping(
            value = "/{userId}/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Create a profile",
            description = "Create a profile for the specified user with image",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Multipart form with image file",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = ProfileRequestDto.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to create profile")
            }
    )
    public ResponseEntity<ApiResponse> createProfilePicture(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") final Long userId,

            @Valid @RequestPart("data") final ProfileRequestDto profileRequest,  // JSON part

            @Parameter(hidden = true)
            @RequestPart(value = "file",required = false) final MultipartFile file,// File part
            final HttpServletRequest request
    ) {
        try {
            final ApiResponse response = this.profileService.createProfile(userId,profileRequest,file);
            return ResponseUtils.buildResponse(request, response);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(
                    request,
                    ApiErrorResponse.error(HttpStatus.BAD_REQUEST.value(), "failed to create profile")
            );
        }
    }



    @DeleteMapping("/{userId}/delete")
    @Operation(summary = "Soft delete profile",
            description = "Soft delete by updating status to INACTIVE",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to delete profile")
            }
    )
    public ResponseEntity<?> softDeleteProfile(
            @PathVariable("userId") final Long userId,
            HttpServletRequest request
    ) {
        try {
            ApiResponse response = this.profileService.softDeleteProfile(userId);
            return ResponseUtils.buildResponse(request, response);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(request,
                    ApiErrorResponse.error(HttpStatus.NOT_FOUND.value(), "Failed to delete profile"));
        }
    }

    @PostMapping(
            value = "/{userId}/update",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Update profile data",
            description = "Update a user's profile information. ( no image) ",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProfileRequestDto.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input or error updating profile")
            }
    )
    public ResponseEntity<ApiResponse> updateProfile(
            @PathVariable("userId") final Long userId,
            @Valid @RequestBody ProfileRequestDto profileRequest,
            HttpServletRequest request
    ) {
        try {
            ApiResponse response = this.profileService.updateProfile(userId, profileRequest);
            return ResponseUtils.buildResponse(request, response);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(request,
                    ApiErrorResponse.error(HttpStatus.BAD_REQUEST.value(), "Invalid input or error updating profile"));
        }
    }



    @PostMapping(
            value = "/{userId}/profile-picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Upload profile picture",
            description = "Uploads a user's profile picture",
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
            @RequestParam(value = "file",required = false) final MultipartFile file
    ) {
        try {
            final String fileUrl = this.profileService.uploadProfilePicture(userId, file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get profile by ID",
            description = "Fetch the profile details of a specific user by profile ID.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Profile not found")
            }
    )
    public ResponseEntity<?> getProfileById(
            @PathVariable("id") Long id,
            HttpServletRequest request
    ) {
        try {
            ApiResponse response = this.profileService.getProfileById(id);
            return ResponseUtils.buildResponse(request, response);
        } catch (Exception e) {
            return ResponseUtils.buildResponse(request,
                    ApiErrorResponse.error(HttpStatus.NOT_FOUND.value(), "Profile not found"));
        }
    }




}
