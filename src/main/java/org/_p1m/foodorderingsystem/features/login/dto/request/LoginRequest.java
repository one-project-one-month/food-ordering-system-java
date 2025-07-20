package org._p1m.foodorderingsystem.features.login.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
