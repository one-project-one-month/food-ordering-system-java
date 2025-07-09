package org._p1m.foodorderingsystem.features.menus.service.impl;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.categories.repository.CategoryRepository;
import org._p1m.foodorderingsystem.features.menus.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menus.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menus.service.ManageMenuService;
import org._p1m.foodorderingsystem.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManageMenuServiceImpl implements ManageMenuService {

    @Override
    public ApiResponse createMenu(CreateMenuRequest createMenuRequest) {

        return null;
    }
}
