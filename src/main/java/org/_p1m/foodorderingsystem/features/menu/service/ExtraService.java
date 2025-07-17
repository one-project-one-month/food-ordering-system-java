package org._p1m.foodorderingsystem.features.menu.service;

import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.menu.dto.request.ExtraRequest;
import org._p1m.foodorderingsystem.features.menu.dto.request.UpdateExtraRequest;
import org.springframework.stereotype.Service;

@Service
public interface ExtraService {
    ApiResponse createExtra(@Valid ExtraRequest extraRequest);

    ApiResponse updateDishSize(Long extraId, @Valid UpdateExtraRequest updateExtraRequest);

    ApiResponse deleteMenu(Long extraId);
}
