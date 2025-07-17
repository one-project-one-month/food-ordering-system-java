package org._p1m.foodorderingsystem.features.menu.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.menu.dto.request.DishSizeRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateDishSizeRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface DishSizeService {
    ApiResponse createDishSize(DishSizeRequest dishSizeRequest);

    String uploadDishSizeImage(Long dishSizeId, MultipartFile file);

    ApiResponse updateDishSize(Long dishSizeId,UpdateDishSizeRequest updateDishSizeRequest);

    ApiResponse deleteMenu(Long dishSizeId);
}
