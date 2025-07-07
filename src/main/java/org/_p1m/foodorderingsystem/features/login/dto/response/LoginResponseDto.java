package org._p1m.foodorderingsystem.features.login.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class LoginResponseDto {
    private long success;
    private long code;
    private ArrayList<Objects>meta;
    private ArrayList<Objects>data;
    private ArrayList<String>message;
}
