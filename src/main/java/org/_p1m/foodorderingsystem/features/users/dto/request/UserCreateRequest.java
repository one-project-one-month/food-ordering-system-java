package org._p1m.foodorderingsystem.features.users.dto.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
}
