package org._p1m.foodorderingsystem.features.restaurant.service.impl;

import org._p1m.foodorderingsystem.common.constant.Status;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.restaurant.dto.response.RestaurantResponseDto;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.features.restaurant.service.RestaurantService;
import org._p1m.foodorderingsystem.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        // I used ModelMapper to convert Restaurant to DTO instead of doing it manually.


        List<RestaurantResponseDto> responseDtos = restaurants.stream()
                .filter(r -> r.getStatus() == Status.ACTIVE) 
 
                .map(r -> modelMapper.map(r, RestaurantResponseDto.class))
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Restaurant list fetched successfully.")
                .data(Map.of("restaurants", responseDtos))
                .build();
    }
}
