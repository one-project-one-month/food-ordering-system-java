package org._p1m.foodorderingsystem.features.restaurant.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.RestaurantCreateRequest;
import org._p1m.foodorderingsystem.features.restaurant.dto.request.RestaurantUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface RestaurantRegistrationService {
    ApiResponse createRestaurant(RestaurantCreateRequest restaurantRequest);

    ApiResponse restaurantDetail(Long id);

    ApiResponse deleteRestaurant(Long id);

    ApiResponse updateRestaurant(Long id, RestaurantUpdateRequest restaurantUpdateRequest);

    String uploadRestaurantPicture(Long restaurantId, MultipartFile file);
}
