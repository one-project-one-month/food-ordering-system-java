package org._p1m.foodorderingsystem.features.users.dto.request;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;
}
