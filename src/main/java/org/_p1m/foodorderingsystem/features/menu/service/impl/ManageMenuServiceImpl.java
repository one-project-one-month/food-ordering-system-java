package org._p1m.foodorderingsystem.features.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.category.repository.CategoryRepository;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.dto.respones.MenuResponse;
import org._p1m.foodorderingsystem.features.menu.dto.response.CreateMenuResponseDto;
import org._p1m.foodorderingsystem.features.menu.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menu.service.ManageMenuService;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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

    @Override
    public String uploadMenuImage(Long menuId, MultipartFile file) {
        Menu menu = manageMenuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        if(file.isEmpty()){
           throw new IllegalArgumentException("File is empty!");
        }

        try {
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = "menu_" + menuId + "_" + System.currentTimeMillis() + fileExtension;

            Path uploadDir = Paths.get("uploads/menu-images");

            Files.createDirectories(uploadDir);

            Path filePath = uploadDir.resolve(newFileName);
            Files.write(filePath, file.getBytes());

            menu.setDishImg(newFileName);
            manageMenuRepository.save(menu);

            return newFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public ApiResponse getMenuById(Long id) {
        Menu menu = manageMenuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        CreateMenuResponseDto dto = modelMapper.map(menu, CreateMenuResponseDto.class);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("menu", dto))
                .message("Menu retrieved successfully").build();
    }

    @Override
    public ApiResponse getAllMenus() {
        List<Menu> menus = manageMenuRepository.findAll();

        List<MenuResponse> menuResponses = menus.stream()
                .map(menu -> modelMapper.map(menu, MenuResponse.class))
                .toList();

        return ApiResponse.builder()
                .success(1).code(HttpStatus.OK.value())
                .data(Map.of("Menus: ", menuResponses))
                .message("All menus retrieved successfully.").build();
    }

    @Override
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
        menu.setDishImg(request.getDishImg());
        menu.setRestaurant(restaurant);
        menu.setCategory(category);

        manageMenuRepository.save(menu);

        CreateMenuResponseDto responseDto = modelMapper.map(menu, CreateMenuResponseDto.class);

        return ApiResponse.builder()
                .success(1).code(HttpStatus.OK.value())
                .data(Map.of("Updated menu: ", responseDto))
                .message("Menu updated successfully.").build();
    }

    @Override
    public ApiResponse deleteMenu(Long id) {
        Menu menu = manageMenuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        manageMenuRepository.delete(menu);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .message("Menu Deleted successfully.").build();
    }
}
