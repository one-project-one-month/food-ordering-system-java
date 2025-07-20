package org._p1m.foodorderingsystem.features.users.service;

import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.users.dto.request.DashboardRequestDto;
import org._p1m.foodorderingsystem.features.users.dto.response.AdminDashboardResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface DashboardService {
	PaginatedApiResponse<AdminDashboardResponseDto> getAdminDashboard(final HttpServletRequest request,
			final String authHeader, final DashboardRequestDto dashboardRequest);
}
