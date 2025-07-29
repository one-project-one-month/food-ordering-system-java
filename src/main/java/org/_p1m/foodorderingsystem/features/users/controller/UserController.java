package org._p1m.foodorderingsystem.features.users.controller;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org._p1m.foodorderingsystem.common.util.JWTUtil;
import org._p1m.foodorderingsystem.common.util.ServerUtil;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.users.dto.request.AuthRequestDto;
import org._p1m.foodorderingsystem.features.users.dto.request.UploadProfilePictureRequest;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org._p1m.foodorderingsystem.features.users.dto.response.AuthResponseDto;
import org._p1m.foodorderingsystem.features.users.service.UserService;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
	private final AuthenticationManager authManager;
	private final ServerUtil serverUtil;
	private final JWTUtil jwtUtil;

	@PostMapping("/login")
	@Operation(
			summary = "Login User",
			description = "Verifies if the provided email and password exists or is valid",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Login request",
					required = true,
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "VerifyEmailRequest",
									summary = "Example request",
									value = "{ \"email\": \"user@example.com\", \"password\": \"123456\" }"
							)
					)
			),
			responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verified Email successfully"),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
			}
	)
	public ResponseEntity<ApiResponse> verifyUser(@RequestBody AuthRequestDto requestDto , HttpServletRequest request){
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
		String token =serverUtil.generateToken((UserDetails)  authentication.getPrincipal());
		String refreshToken = serverUtil.generateRefreshToken((UserDetails)  authentication.getPrincipal());
		final ApiResponse response = this.userService.getUserAuthData(requestDto , token , refreshToken);
		return ResponseUtils.buildResponse(request , response);
	}
    
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

	@PostMapping("/verifyEmail")
	@Operation(
			summary = "Verify Email",
			description = "Verifies if the provided email exists or is valid",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Verify Email request",
					required = true,
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "VerifyEmailRequest",
									summary = "Example request",
									value = "{ \"email\": \"user@example.com\" }"
							)
					)
			),
			responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verified Email successfully"),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
			}
	)
	public ResponseEntity<ApiResponse> verifyEmail(@RequestBody Map<String , String> body, HttpServletRequest request){
		String email = body.get("email");
		final ApiResponse response = this.userService.verifyEmail(email);
		return ResponseUtils.buildResponse(request , response);
	}

	@PostMapping("/verifyAccount")
	@Operation(
			summary = "Verify Account",
			description = "Verifies if the provided email and OTP exists or is valid",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Verify Account request",
					required = true,
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "VerifyEmailRequest",
									summary = "Example request",
									value = "{ \"email\": \"user@example.com\", \"code\": \"123456\" }"
							)
					)
			),
			responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verified Email successfully"),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
			}
	)
	public ResponseEntity<ApiResponse> verifyAccount(@RequestBody Map<String, String> body , HttpServletRequest request){
			long code = Long.parseLong(body.get("code"));
			String email = body.get("email");
			final ApiResponse response = this.userService.verifyAccount(code, email);
			return ResponseUtils.buildResponse(request, response);
    }

	@PostMapping("resendCode")
	@Operation(
			summary = "Resend Code",
			description = "Verifies if the provided Email exists or is valid",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Resend Code request",
					required = true,
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "VerifyEmailRequest",
									summary = "Example request",
									value = "{ \"email\": \"user@example.com\" }"
							)
					)
			),
			responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verified Email successfully"),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
			}
	)
	public ResponseEntity<ApiResponse> resendCode(@RequestBody Map<String ,String> body , HttpServletRequest request){
		final ApiResponse response = this.userService.resendCode(body.get("email"));
		return ResponseUtils.buildResponse(request, response);
	}


	// To Test Token Data
	@PostMapping("/extractToken")
	public Claims extractToken(@RequestBody Map<String , String> body){
		String token = body.get("token");
		return Jwts.parserBuilder()
				.setSigningKey(serverUtil.getSecretKey().getBytes(StandardCharsets.UTF_8))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	@PostMapping("/getRefreshToken")
	@Operation(
			summary = "Get Refresh Token",
			description = "Get Refresh Token after Access Token Expired.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Get Refresh Token",
					required = true,
					content = @Content(
							mediaType = "application/json",
							examples = @ExampleObject(
									name = "VerifyEmailRequest",
									summary = "Example request",
									value = "{ \"email\": \"user@example.com\", \"password\": \"123456\" }"
							)
					)
			),
			responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Generate Refresh Token Successfully"),
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
			}
	)
	public ResponseEntity<ApiResponse> getRefreshToken(@RequestBody AuthRequestDto requestDto , HttpServletRequest request){
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken
				(requestDto.getEmail() , requestDto.getPassword()));
		String refreshToken = serverUtil.generateRefreshToken((UserDetails) authentication.getPrincipal());
		final ApiResponse response = this.userService.getRefreshToken(requestDto , refreshToken);
		return ResponseUtils.buildResponse(request , response);
	}
}
