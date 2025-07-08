package org._p1m.foodorderingsystem.features.restaurant_registration.srevice;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.request.RestaurantCreateRequest;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.request.RestaurantUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantRegistrationService {
    ApiResponse createRestaurant(RestaurantCreateRequest restaurantRequest);

    ApiResponse restaurantDetail(Long id);

    ApiResponse deleteRestaurant(Long id);

    ApiResponse updateRestaurant(Long id, RestaurantUpdateRequest restaurantUpdateRequest);
}
