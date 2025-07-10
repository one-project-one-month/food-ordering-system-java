package org._p1m.foodorderingsystem.features.menu.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManageMenuServiceImpl implements ManageMenuService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ManageMenuRepository manageMenuRepository;
    private final ModelMapper modelMapper;
    private StorageService storageService;

    @Autowired
    public void setStorageService(StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
    }

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

    @Override
    @Transactional
    public String uploadMenuImage(Long menuId, MultipartFile file) {
        final Menu menu = this.manageMenuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found for  ID: " + menuId));

        final String filename = storageService.store(file);

        final String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();

        menu.setDishImg(fileUrl);
        this.manageMenuRepository.save(menu);

        return fileUrl;
    }
}
