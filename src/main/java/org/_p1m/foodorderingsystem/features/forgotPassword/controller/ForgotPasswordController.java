package org._p1m.foodorderingsystem.features.forgotPassword.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org._p1m.foodorderingsystem.features.forgotPassword.dto.request.ForgotPasswordRequest;
import org._p1m.foodorderingsystem.features.forgotPassword.service.ForgotPasswordService;
import org._p1m.foodorderingsystem.features.users.dto.request.UploadProfilePictureRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.base.path}/forgotPassword")
@RequiredArgsConstructor
@Tag(name = "ForgotPassword API", description = "Endpoints for forgotPassword Features")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    @Operation(
            summary = "Forgot Password.",
            description = "Send Forgot Password Mail",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Forgot Password Request",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ForgotPasswordRequest",
                                    summary = "Example request",
                                    value = "{ \"email\": \"user@example.com\"}"
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reset Code Sent to your Email."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<String> sendResetCode(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        forgotPasswordService.sendResetCode(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Reset Code Sent to your Email.");
    }

    @PostMapping("/verifyCode")
    @Operation(
            summary = "Verifying Code",
            description = "Verifying Code",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Verifying Code Request",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "VerifyCodeRequest",
                                    summary = "Example request",
                                    value = "{ \"email\": \"user@example.com\"}"
                            )
                    )
            ),
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Verified Code Sent Successfully."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<String> verifyResetCode(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        boolean valid = forgotPasswordService.verifyResetCode(forgotPasswordRequest.getEmail() , forgotPasswordRequest.getCode());
        if(valid){
            return ResponseEntity.ok("Verified Code Sent Successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or Expired code.");
        }
    }
}