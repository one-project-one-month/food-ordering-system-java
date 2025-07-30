package org._p1m.foodorderingsystem.features.addCart.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;

public interface AddCartMenuService {
	ApiResponse addToCart(AddCartMenuRequest request);
	ApiResponse removeFromCart(Long id);
	ApiResponse forceRemoveFromCart();
}
