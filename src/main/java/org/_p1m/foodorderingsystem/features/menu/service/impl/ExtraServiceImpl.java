package org._p1m.foodorderingsystem.features.menu.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.menu.dto.request.ExtraRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateDishSizeRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateExtraRequest;
import org._p1m.foodorderingsystem.features.menu.dto.responses.ExtraResponse;
import org._p1m.foodorderingsystem.features.menu.repository.ExtraRepo;
import org._p1m.foodorderingsystem.features.menu.repository.ManageMenuRepository;
import org._p1m.foodorderingsystem.features.menu.service.ExtraService;
import org._p1m.foodorderingsystem.model.DishSize;
import org._p1m.foodorderingsystem.model.Extra;
import org._p1m.foodorderingsystem.model.Menu;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExtraServiceImpl implements ExtraService {

    private final ManageMenuRepository menuRepo;
    private final ExtraRepo extraRepo;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createExtra(ExtraRequest extraRequest) {
        Menu menu = this.menuRepo.findById(extraRequest.getMenuId())
                .orElseThrow(()-> new EntityNotFoundException("Menu not found for id"+ extraRequest.getMenuId()));
        Extra extra = new Extra();
        extra.setName(extraRequest.getName());
        extra.setPrice(extraRequest.getPrice());
        extra.setMenu(menu);

        this.extraRepo.save(extra);

        ExtraResponse dto = modelMapper.map(extra, ExtraResponse.class);
        return ApiResponse.builder().success(1).code(HttpStatus.CREATED.value())
                .data(Map.of("created Extra", dto))
                .message("Extra created successful for menu id" + extraRequest.getMenuId()).build();
    }

    @Override
    public ApiResponse updateDishSize(Long extraId, UpdateExtraRequest updateExtraRequest) {
        Extra extra = this.extraRepo.findById(extraId)
                .orElseThrow(()-> new EntityNotFoundException("Extra not found"));

        extra.setName(updateExtraRequest.getName());
        extra.setPrice(updateExtraRequest.getPrice());

        this.extraRepo.save(extra);

        UpdateExtraRequest dto = modelMapper.map(extra,UpdateExtraRequest.class);

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("updated Extra", dto))
                .message("Extra updated successful")
                .build();
    }

    @Override
    public ApiResponse deleteMenu(Long extraId) {
        Extra extra = this.extraRepo.findById(extraId)
                .orElseThrow(() -> new EntityNotFoundException("Extra not found!"));

        this.extraRepo.delete(extra);

        return ApiResponse.builder().success(1)
                .code(HttpStatus.OK.value())
                .message("Extra Deleted successfully.").build();
    }
}
