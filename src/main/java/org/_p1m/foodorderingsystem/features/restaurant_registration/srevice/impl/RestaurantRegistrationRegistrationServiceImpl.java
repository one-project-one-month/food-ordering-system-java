package org._p1m.foodorderingsystem.features.restaurant_registration.srevice.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.exceptions.DuplicateEntityException;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.request.RestaurantCreateRequest;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.request.RestaurantUpdateRequest;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.response.RestaurantDetailResponseDto;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.response.RestaurantResponseDto;
import org._p1m.foodorderingsystem.features.restaurant_registration.dto.response.RestaurantUpdateResponseDto;
import org._p1m.foodorderingsystem.features.restaurant_registration.repository.RestaurantRegistrationRepository;
import org._p1m.foodorderingsystem.features.restaurant_registration.srevice.RestaurantRegistrationService;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.Restaurant;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantRegistrationRegistrationServiceImpl implements RestaurantRegistrationService {

  private final UserRepository userRepository;
  private final RestaurantRegistrationRepository restaurantRegistrationRepository;
  private final ModelMapper modelMapper;


    @Transactional
    @Override
    public ApiResponse createRestaurant(RestaurantCreateRequest restaurantRequest) {
        User restaurantOwner = this.userRepository.findById(restaurantRequest.getResOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("No user found"));

        Optional<Restaurant> exitedRestaurant = this.restaurantRegistrationRepository.findByOwnerId(restaurantRequest.getResOwnerId());

        if(exitedRestaurant.isPresent()){
            throw new DuplicateEntityException("Owner can create one restaurant");
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(restaurantOwner);
        restaurant.setNrc(restaurantRequest.getNrc());
        restaurant.setRestaurantName(restaurantRequest.getRestaurantName());
        restaurant.setRestaurantImage(restaurantRequest.getRestaurantImage());
        restaurant.setContactNumber(restaurantRequest.getContactNumber());
        restaurant.setKpayNumber(restaurantRequest.getKpayNumber());
        restaurantRegistrationRepository.save(restaurant);
        RestaurantResponseDto dto = modelMapper.map(restaurant, RestaurantResponseDto.class);
        dto.setResOwnerId(restaurantOwner.getId());
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("createdRestaurant", dto))
                .message("Restaurant created.").build();

    }

    @Override
    public ApiResponse restaurantDetail(Long id) {
        Restaurant restaurant = this.restaurantRegistrationRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id" + id));

        RestaurantDetailResponseDto dto = modelMapper.map(restaurant,RestaurantDetailResponseDto.class);
        dto.setResOwnerId(restaurant.getOwner().getId());

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("restaurantDetail", dto))
                .message("Restaurant Detail")
                .build();

    }

    @Override
    public ApiResponse deleteRestaurant(Long id) {
        Restaurant restaurant = this.restaurantRegistrationRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id" + id));
        restaurant.setStatus(Status.INACTIVE);
        this.restaurantRegistrationRepository.save(restaurant);
        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Restaurant deleted successfully with id" + id)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse updateRestaurant(Long id, RestaurantUpdateRequest restaurantUpdateRequest) {
        Restaurant restaurant = this.restaurantRegistrationRepository.findByIdAndStatus(id,Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id" + id));

        restaurant.setId(id);
        restaurant.setNrc(restaurantUpdateRequest.getNrc());
        restaurant.setRestaurantName(restaurantUpdateRequest.getRestaurantName());
        restaurant.setRestaurantImage(restaurantUpdateRequest.getRestaurantImage());
        restaurant.setContactNumber(restaurantUpdateRequest.getContactNumber());
        restaurant.setKpayNumber(restaurantUpdateRequest.getKpayNumber());
        restaurantRegistrationRepository.save(restaurant);
        RestaurantUpdateResponseDto dto = modelMapper.map(restaurant,RestaurantUpdateResponseDto.class);

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("updated Restaurant", dto))
                .message("Restaurant Update successful")
                .build();
    }
}
