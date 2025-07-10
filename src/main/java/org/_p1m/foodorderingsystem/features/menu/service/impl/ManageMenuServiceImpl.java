package org._p1m.foodorderingsystem.features.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.category.repository.CategoryRepository;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.dto.response.CreateMenuResponseDto;
import org._p1m.foodorderingsystem.features.menu.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menu.service.ManageMenuService;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.model.*;
import org.modelmapper.ModelMapper;
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



        createMenuRequest.getDishSizes().forEach(dishSizeRequest -> {
         DishSize dishSize =   modelMapper.map(dishSizeRequest, DishSize.class);
         menu.addDishSize(dishSize);
        });
        createMenuRequest.getExtras().forEach(extraRequest -> {
            Extra extra = modelMapper.map(extraRequest, Extra.class);
            menu.addExtra(extra);
        });

        this.manageMenuRepository.save(menu);
        CreateMenuResponseDto dto = modelMapper.map(menu, CreateMenuResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("created Menu", dto))
                .message("Menu created successful.").build();
    }

    public ApiResponse getMenuById(Long id) {
        Menu menu = manageMenuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        CreateMenuResponseDto dto = modelMapper.map(menu, CreateMenuResponseDto.class);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("menu", dto))
                .message("Menu retrieved successfully").build();
    }

    public ApiResponse updateMenu(Long menuId, CreateMenuRequest request) {
        Menu menu = manageMenuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found!"));

        Category category = categoryRepository.findByIdAndRestaurantId(request.getCategoryId(), request.getRestaurantId())
                .orElseThrow(()-> new EntityNotFoundException("Category not found for this restaurant!"));

        menu.setDish(request.getDish());
        menu.setPrice(request.getPrice());
        menu.setStatus(request.getStatus());
        menu.setDishImg(request.get);

    }

    public ApiResponse deleteMenu(Long id) {
        Menu menu = manageMenuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        manageMenuRepository.delete(menu);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .message("Menu Deleted successfully.").build();
    }
}
