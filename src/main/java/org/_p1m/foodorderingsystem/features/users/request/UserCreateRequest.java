package org._p1m.foodorderingsystem.features.users.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    private String email;
    private String password;
    private String name;
    private String phone;
}
