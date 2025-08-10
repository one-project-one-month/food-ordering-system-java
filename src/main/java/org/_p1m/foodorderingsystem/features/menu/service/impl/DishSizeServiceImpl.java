package org._p1m.foodorderingsystem.features.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.menu.dto.request.DishSizeRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateDishSizeRequest;
import org._p1m.foodorderingsystem.features.menu.dto.responses.DishSizeResponse;
import org._p1m.foodorderingsystem.features.menu.repository.DishSizeRepo;
import org._p1m.foodorderingsystem.features.menu.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menu.service.DishSizeService;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Menu;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DishSizeServiceImpl implements DishSizeService {

    private final ManageMenuRepository manageMenuRepository;
    private final DishSizeRepo dishSizeRepo;
    private final ModelMapper modelMapper;
    private  StorageService storageService;

    @Autowired
    public void setStorageService(StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
    }

    @Override
    public ApiResponse createDishSize(DishSizeRequest dishSizeRequest) {
        Menu menu = this.manageMenuRepository.findById(dishSizeRequest.getMenuId())
                .orElseThrow(()-> new EntityNotFoundException("Menu not found for id"+ dishSizeRequest.getMenuId()));
        DishSize dishSize = new DishSize();
        dishSize.setName(dishSizeRequest.getName());
        dishSize.setPrice(dishSizeRequest.getPrice());
        dishSize.setMenu(menu);

        this.dishSizeRepo.save(dishSize);

        DishSizeResponse dto = modelMapper.map(dishSize, DishSizeResponse.class);
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("created DishSize", dto))
                .message("Dish Size created successful for menu id" + dishSizeRequest.getMenuId()).build();
    }

    public void deleteFromLocalFolder(String  existingUrl) {
        if (existingUrl != null && !existingUrl.isEmpty()) {
            String existingFileName = existingUrl.substring(existingUrl.lastIndexOf("/") + 1);
            String decodedFilename = URLDecoder.decode(existingFileName, StandardCharsets.UTF_8);
            storageService.delete(decodedFilename);
        }
    }

    @Override
    public String uploadDishSizeImage(Long dishSizeId, MultipartFile file) {
        DishSize dishSize = this.dishSizeRepo.findById(dishSizeId)
                .orElseThrow(() -> new EntityNotFoundException("Dish Size not found!"));

        deleteFromLocalFolder(dishSize.getDishSizeImg());

        final String filename = storageService.store(file);

        final String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();

        dishSize.setDishSizeImg(fileUrl);
        this.dishSizeRepo.save(dishSize);

        return fileUrl;
    }

    @Override
    public ApiResponse updateDishSize(Long dishSizeId,UpdateDishSizeRequest updateDishSizeRequest) {
        DishSize dishSize = this.dishSizeRepo.findById(dishSizeId)
                .orElseThrow(()-> new EntityNotFoundException("Dish size not found"));

        dishSize.setName(updateDishSizeRequest.getName());
        dishSize.setPrice(updateDishSizeRequest.getPrice());

        this.dishSizeRepo.save(dishSize);

        UpdateDishSizeRequest dto = modelMapper.map(dishSize,UpdateDishSizeRequest.class);

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("updated DishSize", dto))
                .message("Dish Size updated successful")
                .build();

    }

    @Override
    public ApiResponse deleteMenu(Long dishSizeId) {
        DishSize dishSize = this.dishSizeRepo.findById(dishSizeId)
                .orElseThrow(() -> new EntityNotFoundException("Dish Size not found!"));

        deleteFromLocalFolder(dishSize.getDishSizeImg());
        this.dishSizeRepo.delete(dishSize);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .message("Dish Size Deleted successfully.").build();
    }


}
