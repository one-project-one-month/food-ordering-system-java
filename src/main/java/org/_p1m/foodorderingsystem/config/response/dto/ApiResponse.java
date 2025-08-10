package org._p1m.foodorderingsystem.config.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiResponse {
    private int success;
    private int code;
    private Map<String, Object> meta;
    private Object data;
    private String message;

    public ApiResponse(int success, int code, Map<String, Object> meta, Object data, String message) {
        this.success = success;
        this.code = code;
        this.meta = meta;
        this.data = data;
        this.message = message;
    }
}