package org._p1m.foodorderingsystem.features.menus.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.categories.repository.CategoryRepository;
import org._p1m.foodorderingsystem.features.menus.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menus.dto.response.CreateMenuResponseDto;
import org._p1m.foodorderingsystem.features.menus.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menus.service.ManageMenuService;
import org._p1m.foodorderingsystem.features.restaurants.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.features.users.dto.response.UserResponseDto;
import org._p1m.foodorderingsystem.model.Category;
import org._p1m.foodorderingsystem.model.Menu;
import org._p1m.foodorderingsystem.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManageMenuServiceImpl implements ManageMenuService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ManageMenuRepository manageMenuRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createMenu(CreateMenuRequest createMenuRequest) {

        Restaurant restaurant = this.restaurantRepository.findById(createMenuRequest.getRestaurantId())
                .orElseThrow(()-> new EntityNotFoundException("No restaurant found"));

        Category category = this.categoryRepository.findByIdAndRestaurantId(createMenuRequest.getCategoryId(),
                                                                            createMenuRequest.getRestaurantId())
                .orElseThrow(()-> new EntityNotFoundException("No category found at this restaurant"));

        Menu menu = new Menu();
        menu.setDish(createMenuRequest.getDish());
        menu.setPrice(createMenuRequest.getPrice());
        menu.setStatus(createMenuRequest.getStatus());
        menu.setDishImg(createMenuRequest.getDishImg());
        menu.setCategory(category);
        menu.setRestaurant(restaurant);

        createMenuRequest.getDishSizes().forEach(menu::addDishSize);
        createMenuRequest.getExtras().forEach(menu::addExtra);

        this.manageMenuRepository.save(menu);
        CreateMenuResponseDto dto = modelMapper.map(menu, CreateMenuResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("created Menu", dto))
                .message("Menu created successful.").build();
    }
}
