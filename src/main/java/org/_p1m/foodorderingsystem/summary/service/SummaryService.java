package org._p1m.foodorderingsystem.summary.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface SummaryService {
    ApiResponse getOwnerSummary(HttpServletRequest request);
    ApiResponse getDeliverySummary(HttpServletRequest request);
}