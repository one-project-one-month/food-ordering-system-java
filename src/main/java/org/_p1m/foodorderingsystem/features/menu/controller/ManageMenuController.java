package org._p1m.foodorderingsystem.features.menu.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.features.menu.dto.request.CreateMenuRequest;
import org._p1m.foodorderingsystem.features.menu.service.ManageMenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.path}/auth/menus")
public class ManageMenuController {

    private  final ManageMenuService manageMenuService;

    ManageMenuController(ManageMenuService manageMenuService){
        this.manageMenuService = manageMenuService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createMenu(@Valid @RequestBody CreateMenuRequest createMenuRequest, HttpServletRequest request){
        final ApiResponse response = this.manageMenuService.createMenu(createMenuRequest);
        return ResponseUtils.buildResponse(request, response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id, HttpServletRequest request) {
        final ApiResponse response = this.manageMenuService.getMenuById(id);
        return ResponseUtils.buildResponse(request, response);
    }
}
