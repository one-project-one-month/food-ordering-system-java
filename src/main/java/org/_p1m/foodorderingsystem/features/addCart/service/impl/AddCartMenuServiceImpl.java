package org._p1m.foodorderingsystem.features.addCart.service.impl;

import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;
import org._p1m.foodorderingsystem.features.addCart.repository.AddCartMenuRepo;
import org._p1m.foodorderingsystem.model.AddCartData;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class AddCartMenuServiceImpl {

    private AddCartMenuRepo addCartMenuRepo;

    public ApiResponse getCartById(Long id) {
        AddCartData addCartData = addCartMenuRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found!"));

        return ApiResponse.builder()
                .success(1).code(HttpStatus.OK.value())
                .data(Map.of("Cart data: ", addCartData))
                .message("Cart retrieved.").build();
    }
}
