package org._p1m.foodorderingsystem.features.menu.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ManageMenuService {
    ApiResponse createMenu(CreateMenuRequest createMenuRequest);

    String uploadMenuImage(Long menuId, MultipartFile file);
}
