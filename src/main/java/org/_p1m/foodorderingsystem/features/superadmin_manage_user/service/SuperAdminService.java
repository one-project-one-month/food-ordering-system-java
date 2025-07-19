package org._p1m.foodorderingsystem.features.superadmin_manage_user.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.DeletedUserResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.SuperAdminDashBoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuperAdminService {

    public ApiResponse deleteById(final Long id);

    PaginatedApiResponse<SuperAdminDashBoardResponse> getAllUsersPaginated(
            String keyword, String role, String status, Pageable pageable);

}
