package org._p1m.foodorderingsystem.features.menus.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.menus.dto.request.CreateMenuRequest;
import org.springframework.stereotype.Service;

@Service
public interface ManageMenuService {
    ApiResponse createMenu(CreateMenuRequest createMenuRequest);
}
