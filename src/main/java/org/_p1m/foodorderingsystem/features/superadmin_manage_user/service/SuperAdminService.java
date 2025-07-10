package org._p1m.foodorderingsystem.features.superadmin_manage_user.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.DeletedUserResponse;

public interface SuperAdminService {

    public ApiResponse deleteById(final Long id);
}
