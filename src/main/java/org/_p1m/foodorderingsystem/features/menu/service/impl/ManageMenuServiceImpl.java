package org._p1m.foodorderingsystem.features.menu.service.impl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.Predicate;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginationMeta;
import org._p1m.foodorderingsystem.features.category.repository.CategoryRepository;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.GetAllMenuRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.dto.responses.MenuResponseDto;
import org._p1m.foodorderingsystem.features.menu.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menu.service.ManageMenuService;
import org._p1m.foodorderingsystem.features.restaurant.repository.RestaurantRepository;
import org._p1m.foodorderingsystem.model.Category;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;
import org._p1m.foodorderingsystem.model.Menu;
import org._p1m.foodorderingsystem.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

//        createMenuRequest.getDishSizes().forEach(dishSizeRequest -> {
//         DishSize dishSize =   modelMapper.map(dishSizeRequest, DishSize.class);
//         menu.addDishSize(dishSize);
//        });
//        createMenuRequest.getExtras().forEach(extraRequest -> {
//            Extra extra = modelMapper.map(extraRequest, Extra.class);
//            menu.addExtra(extra);
//        });

        this.manageMenuRepository.save(menu);
        MenuResponseDto dto = modelMapper.map(menu, MenuResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("created Menu", dto))
                .message("Menu created successful.").build();
    }

    private void deleteFromLocalFolder(String  existingUrl) {
        if (existingUrl != null && !existingUrl.isEmpty()) {
            String existingFileName = existingUrl.substring(existingUrl.lastIndexOf("/") + 1);
            String decodedFilename = URLDecoder.decode(existingFileName, StandardCharsets.UTF_8);
            storageService.delete(decodedFilename);
        }
    }

    @Override
    public String uploadMenuImage(Long menuId, MultipartFile file) {
        Menu menu = manageMenuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        deleteFromLocalFolder(menu.getDishImg());

        final String filename = storageService.store(file);

        final String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();

        menu.setDishImg(fileUrl);
        this.manageMenuRepository.save(menu);

        return fileUrl;
    }

    @Override
    public ApiResponse getMenuById(Long id) {
        Menu menu = manageMenuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        MenuResponseDto dto = modelMapper.map(menu, MenuResponseDto.class);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("menu", dto))
                .message("Menu retrieved successfully").build();
    }

    /*
    @Override
    public PaginatedApiResponse<MenuResponseDto> getAllMenus(GetAllMenuRequest getAllMenuRequest) {

        final int page = getAllMenuRequest.page();
        final int size = getAllMenuRequest.size();
        Pageable pageable = PageRequest.of(page - 1, size);
        Specification<Menu> spec = menuSpecification(getAllMenuRequest);
        Page<Menu> pageResult = manageMenuRepository.findAll(spec, pageable);
        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setCurrentPage(page);
        List<MenuResponseDto> data = pageResult.map(this::mapToDto).toList();
        return PaginatedApiResponse.<MenuResponseDto>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Menus retrieved successfully.")
                .meta(meta)
                .data(data)
                .build();
    }
     */

    @Override
    public PaginatedApiResponse<MenuResponseDto> getAllMenus(GetAllMenuRequest getAllMenuRequest) {
        return getPaginatedMenus(null, getAllMenuRequest);
    }

    private Specification<Menu> menuSpecification(GetAllMenuRequest getAllMenuRequest, Long restaurantId) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (restaurantId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("restaurant").get("id"), restaurantId));
            }

            if (getAllMenuRequest.dish() != null && !getAllMenuRequest.dish().isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("dish")), "%" + getAllMenuRequest.dish().toLowerCase() + "%"));
            }

            if (getAllMenuRequest.price() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("price"), getAllMenuRequest.price()));
            }

            if (getAllMenuRequest.categoryId() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("category").get("id"), getAllMenuRequest.categoryId()));
            }


            if (getAllMenuRequest.status() != null) {
                    Status status = Status.valueOf(getAllMenuRequest.status().toUpperCase());
                    predicate = cb.and(predicate,
                            cb.equal(root.get("status"), status));
            }
            return predicate;
        };
    }

    @Override
    public ApiResponse updateMenu(Long menuId, UpdateMenuRequest request) {
        Menu menu = manageMenuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(()-> new EntityNotFoundException("Category not found!"));

        menu.setDish(request.getDish());
        menu.setPrice(request.getPrice());
        menu.setStatus(request.getStatus());
        menu.setCategory(category);

        menu.getDishSizes().clear();
        menu.getExtras().clear();

        request.getDishSizes().forEach(dishSizeRequest -> {
            DishSize dishSize =   modelMapper.map(dishSizeRequest, DishSize.class);
            menu.addDishSize(dishSize);
        });
        request.getExtras().forEach(extraRequest -> {
            Extra extra = modelMapper.map(extraRequest, Extra.class);
            menu.addExtra(extra);
        });

        manageMenuRepository.save(menu);

        MenuResponseDto responseDto = modelMapper.map(menu, MenuResponseDto.class);

        return ApiResponse.builder()
                .success(1).code(HttpStatus.OK.value())
                .data(Map.of("Updated menu: ", responseDto))
                .message("Menu updated successfully.").build();
    }

    @Override
    public ApiResponse deleteMenu(Long id) {
        Menu menu = manageMenuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found!"));

        deleteFromLocalFolder(menu.getDishImg());
        manageMenuRepository.delete(menu);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .message("Menu Deleted successfully.").build();
    }

    private MenuResponseDto mapToDto(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getDish(),
                menu.getPrice(),
                menu.getDishImg(),
                menu.getStatus(),
                menu.getRestaurant().getId(),
                menu.getCategory().getId(),
                menu.getDishSizes().stream().map(size ->
                        new MenuResponseDto.DishSizeDto(
                                size.getId(),
                                size.getName(),
                                size.getPrice(),
                                size.getDishSizeImg()
                        )
                ).toList(),
                menu.getExtras().stream().map(extra ->
                        new MenuResponseDto.ExtraDto(
                                extra.getId(),
                                extra.getName(),
                                extra.getPrice()
                        )
                ).toList()
        );
    }

    @Override
    public PaginatedApiResponse<MenuResponseDto> getAllMenusByRestaurantId(Long restaurantId, GetAllMenuRequest getAllMenuRequest) {

        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantId));
        return getPaginatedMenus(restaurantId, getAllMenuRequest);
    }

    private PaginatedApiResponse<MenuResponseDto> getPaginatedMenus(Long restaurantId, GetAllMenuRequest getAllMenuRequest) {
        final int page = getAllMenuRequest.page();
        final int size = getAllMenuRequest.size();
        Pageable pageable = PageRequest.of(page - 1, size);
        Specification<Menu> spec = menuSpecification(getAllMenuRequest, restaurantId);
        Page<Menu> pageResult = manageMenuRepository.findAll(spec, pageable);

        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setCurrentPage(page);

        List<MenuResponseDto> data = pageResult.map(this::mapToDto).toList();

        return PaginatedApiResponse.<MenuResponseDto>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Menus retrieved successfully.")
                .meta(meta)
                .data(data)
                .build();
    }
}
