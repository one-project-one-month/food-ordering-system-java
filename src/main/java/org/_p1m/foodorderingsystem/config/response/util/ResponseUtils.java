package org._p1m.foodorderingsystem.config.response.util;

import java.util.HashMap;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.processOrder.dto.response.OrderResponseDTO;
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
    public static Object success(OrderResponseDTO response, String s) {
        return null;
    }
    public static <T> ResponseEntity<PaginatedApiResponse<T>> buildPaginatedResponse(
            final HttpServletRequest request,
            PaginatedApiResponse<T> paginatedResponse) {

        final HttpStatus status = HttpStatus.valueOf(paginatedResponse.getCode());
        if (paginatedResponse.getMeta().getMethod() == null && paginatedResponse.getMeta().getEndpoint() == null) {
            final String method = request.getMethod();
            final String endpoint = request.getRequestURI();
            paginatedResponse.getMeta().setMethod(method);
            paginatedResponse.getMeta().setEndpoint(endpoint);
        }
        return new ResponseEntity<>(paginatedResponse, status);
    }
}