package org._p1m.foodorderingsystem.features.forgotPassword.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String username;
    private String password;
}
