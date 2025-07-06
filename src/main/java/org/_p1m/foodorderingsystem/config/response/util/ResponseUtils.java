package org._p1m.foodorderingsystem.config.response.util;

import java.time.Instant;
import java.util.HashMap;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public class ResponseUtils {

    public static ResponseEntity<ApiResponse> buildResponse(final HttpServletRequest request, final ApiResponse response) {
        final HttpStatus status = HttpStatus.valueOf(response.getCode());

        if (response.getMeta() == null) {
            final String method = request.getMethod();
            final String endpoint = request.getRequestURI();
            response.setMeta(new HashMap<>());
            response.getMeta().put("method", method);
            response.getMeta().put("endpoint", endpoint);
        }

        return new ResponseEntity<>(response, status);
    }
}