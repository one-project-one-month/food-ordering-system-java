package org._p1m.foodorderingsystem.features.restaurant.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;

public interface RestaurantService {
    ApiResponse getAllRestaurants();
    ApiResponse getRestaurantByUserId(Long userId);
}
