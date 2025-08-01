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
                    description = "Multipart form with JSON profile data and optional image file",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object"),
                            examples = @ExampleObject(
                                    name = "Example Profile",
                                    summary = "Example multipart request",
                                    description = "This is a sample JSON payload for the `data` part of the multipart form.",
                                    value = """
                                               {
                                                  "name": "Ko PU",
                                                  "nrc": "12/ABC(N)123456",
                                                  "phone": "09123456789",
                                                  "dob": "1990-01-01",
                                                  "gender": "MALE",
                                                  "address": "No.123, Main Road, Yangon"
                                               }
                                             """
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Profile created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            name = "Success Response",
                                            value = "{\n" +
                                                    "  \"success\": 1,\n" +
                                                    "  \"code\": 200,\n" +
                                                    "  \"meta\": {\n" +
                                                    "    \"endpoint\": \"/api/v1/auth/profile/3/update\",\n" +
                                                    "    \"method\": \"POST\"\n" +
                                                    "  },\n" +
                                                    "  \"data\": {\n" +
                                                    "    \"name\": \"Ko PU\",\n" +
                                                    "    \"nrc\": \"12/ABC(N)123456\",\n" +
                                                    "    \"phone\": \"09123456789\",\n" +
                                                    "    \"dob\": \"1990-01-01\",\n" +
                                                    "    \"gender\": \"MALE\",\n" +
                                                    "    \"profilePic\": \"null\",\n" +
                                                    "    \"address\": \"1990-01-01\"\n" +
                                                    "  },\n" +
                                                    "  \"message\": \"Profile created successfully\"\n" +
                                                    "}"
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Failed to create profile",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            name = "Error response example",
                                            value = "{\n" +
                                                    "  \"success\": 0,\n" +
                                                    "  \"code\": 400,\n" +
                                                    "  \"meta\": null,\n" +
                                                    "  \"data\": null,\n" +
                                                    "  \"message\": \"Failed to create profile\"\n" +
                                                    "}"
                                    )
                            )
                    )
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
    @Operation(
            summary = "Soft delete profile",
            description = "Soft delete by updating status to INACTIVE",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Profile soft-deleted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = """
                    {
                        "success": 1,
                        "code": 200,
                        "meta": {
                            "endpoint": "/api/v1/auth/profile/3/delete",
                            "method": "DELETE"
                        },
                        "data": null,
                        "message": "Profile soft-deleted successfully"
                    }
                """)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Failed to delete profile",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = """
                    {
                        "success": 0,
                        "code": 404,
                        "meta": null,
                        "data": null,
                        "message": "Failed to delete profile"
                    }
                """)
                            )
                    )
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
                            schema = @Schema(implementation = ProfileRequestDto.class),
                            examples = @ExampleObject(
                                       name = "Profile update example",
                                        value = "{\n" +
                                                "  \"name\": \"KoKyiKyaw\",\n" +
                                                "  \"nrc\": \"12/ABC(N)123451\",\n" +
                                                "  \"phone\": \"09123456786\",\n" +
                                                "  \"dob\": \"2001-05-11\",\n" +
                                                "  \"gender\": \"Female\",\n" +
                                                "  \"address\": \"No. 123, Sakura Street, Mandalay\"\n" +
                                                "}"
    )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                            description = "Profile updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            name = "Success Response",
                                            value = "{\n" +
                                                    "  \"success\": 1,\n" +
                                                    "  \"code\": 200,\n" +
                                                    "  \"meta\": {\n" +
                                                    "    \"endpoint\": \"/api/v1/auth/profile/3/update\",\n" +
                                                    "    \"method\": \"POST\"\n" +
                                                    "  },\n" +
                                                    "  \"data\": {\n" +
                                                    "    \"name\": \"KoKyiKyaw\",\n" +
                                                    "    \"nrc\": \"12/ABC(N)123451\",\n" +
                                                    "    \"phone\": \"09123456786\",\n" +
                                                    "    \"dob\": \"2001-05-11\",\n" +
                                                    "    \"gender\": \"Female\",\n" +
                                                    "    \"profilePic\": \"http://localhost:8080/files/https:/res.cloudinary.com/dsylx9zse/image/upload/v1/food_ordering_system/pngkv92rjk6mbbh5fhts\",\n" +
                                                    "    \"address\": \"No. 123, Sakura Street, Mandalay\"\n" +
                                                    "  },\n" +
                                                    "  \"message\": \"Profile updated successfully\"\n" +
                                                    "}"
                                    )
                            )


                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                            description = "Invalid input or error updating profile",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            name = "Error response example",
                                            value = "{\n" +
                                                    "  \"success\": 0,\n" +
                                                    "  \"code\": 400,\n" +
                                                    "  \"meta\": null,\n" +
                                                    "  \"data\": null,\n" +
                                                    "  \"message\": \"Invalid input or error updating profile\"\n" +
                                                    "}"
                                    )
                            )
                    )
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
            summary = "Update profile picture",
            description = "Upload and update user's profile picture",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Multipart form with image file",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = UploadProfilePictureRequest.class)
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "File uploaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(type = "string"),
                                    examples = @ExampleObject(
                                            value = "{ \"url\": \"http://localhost:8080/files/...\" }"
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Failed to upload file",
                            content = @Content(
                                    mediaType = MediaType.TEXT_PLAIN_VALUE,
                                    schema = @Schema(type = "string"),
                                    examples = @ExampleObject(
                                            value = "Failed to update a profile"
                                    )
                            )
                    )
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
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Profile retrieved successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(value = """
                    {
                      "success": 1,
                      "code": 200,
                      "meta": {
                        "endpoint": "/api/v1/auth/profile/4",
                        "method": "GET"
                      },
                      "data": {
                        "name": "Hahah",
                        "nrc": "12/ABC(N)123457",
                        "phone": "09123456788",
                        "dob": "2001-05-10",
                        "gender": "Male",
                        "profilePic": "http://localhost:8080/files/...",
                        "address": "No. 123, Sakura Street, Yangon"
                      },
                      "message": "Profile fetched successfully"
                    }
                """)
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Profile not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(value = """
                    {
                      "success": 0,
                      "code": 404,
                      "meta": null,
                      "data": null,
                      "message": "Profile Not found"
                    }
                """)
                            )
                    )
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
