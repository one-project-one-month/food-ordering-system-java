package org._p1m.foodorderingsystem.features.profile.util;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;

public class ApiErrorResponse {
    public static ApiResponse error(int statusCode, String message) {
        return ApiResponse.builder()
                .success(0)
                .code(statusCode)
                .meta(null)
                .data(null)
                .message(message)
                .build();
    }
}
