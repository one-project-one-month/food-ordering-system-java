package org._p1m.foodorderingsystem.features.superadmin_manage_user.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;

public interface SuperAdminService {

    public ApiResponse deleteById(Long id);
}
