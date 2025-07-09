package org._p1m.foodorderingsystem.features.forgotPassword.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.features.forgotPassword.dto.request.ForgotPasswordRequest;
import org._p1m.foodorderingsystem.features.forgotPassword.service.ForgotPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/forgotPassword")
@RequiredArgsConstructor
@Tag(name = "ForgotPassword API", description = "Endpoints for forgotPassword Features")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    public ResponseEntity<String> sendResetCode(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        forgotPasswordService.sendResetCode(forgotPasswordRequest.getEmail());
        return ResponseEntity.ok("Reset Code Sent to your Email.");
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<String> verifyResetCode(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        boolean valid = forgotPasswordService.verifyResetCode(forgotPasswordRequest.getEmail() , forgotPasswordRequest.getCode());
        if(valid){
            return ResponseEntity.ok("Verified Code Successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or Expired code.");
        }
    }
}
