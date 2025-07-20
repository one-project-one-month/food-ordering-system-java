package org._p1m.foodorderingsystem.features.addCart.service;

import org._p1m.foodorderingsystem.features.addCart.dto.request.AddCartMenuRequest;
import org._p1m.foodorderingsystem.features.addCart.dto.response.AddCartMenuResponse;

public interface AddCartMenuService {
    AddCartMenuResponse addToCart(AddCartMenuRequest request);
}
