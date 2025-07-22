package org._p1m.foodorderingsystem.features.users.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.users.dto.request.DashboardRequestDto;
import org._p1m.foodorderingsystem.features.users.dto.response.AdminDashboardResponseDto;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.features.users.service.DashboardService;
import org._p1m.foodorderingsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
	private final UserRepository userRepository;
	@Override
	public PaginatedApiResponse<AdminDashboardResponseDto> getAdminDashboard(
			final HttpServletRequest request,
			final String authHeader,
			final DashboardRequestDto dashboardRequest) {
		final int page = dashboardRequest.page();
		final int size = dashboardRequest.size();
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<User> pageResult = userRepository.findAll(pageable);
		List<AdminDashboardResponseDto> data = pageResult.getContent()
			    .stream()
			    .map(user -> new AdminDashboardResponseDto(user.getId(), user.getEmail()))
			    .collect(Collectors.toList());
		Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", pageResult.getTotalElements());
        meta.put("totalPages", pageResult.getTotalPages());
        meta.put("currentPage", pageResult);
        return PaginatedApiResponse.<AdminDashboardResponseDto>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("User data for admin dashboard retrieved successfully.")
                .meta(meta)
                .data(data)
                .build();
	}

}
