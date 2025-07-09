package org._p1m.foodorderingsystem.features.menus.controller;


import jakarta.servlet.http.HttpServletRequest;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.menus.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menus.service.ManageMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/auth/menus")
public class ManageMenuController {

    private  final ManageMenuService manageMenuService;

    ManageMenuController(ManageMenuService manageMenuService){
        this.manageMenuService = manageMenuService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createMenu(@RequestBody CreateMenuRequest createMenuRequest, HttpServletRequest request){
        final ApiResponse response = this.manageMenuService.createMenu(createMenuRequest);
        return ResponseUtils.buildResponse(request, response);
    }
}
