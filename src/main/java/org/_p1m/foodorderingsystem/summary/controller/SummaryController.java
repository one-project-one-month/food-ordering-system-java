package org._p1m.foodorderingsystem.summary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.util.ResponseUtils;
import org._p1m.foodorderingsystem.summary.service.SummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base.path}/summary")
@Tag(name = "Summary API", description = "Endpoints for getting summaries")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @GetMapping("/owners")
    @Operation(summary = "Get owner summary", description = "Returns a summary of orders and revenue for the owner.")
    public ResponseEntity<ApiResponse> getOwnerSummary(HttpServletRequest request) {
        ApiResponse response = summaryService.getOwnerSummary(request);
        return ResponseUtils.buildResponse(request, response);
    }

    @GetMapping("/delivery")
    @Operation(summary = "Get delivery summary", description = "Returns a summary of delivered and cancelled orders for delivery staff.")
    public ResponseEntity<ApiResponse> getDeliverySummary(HttpServletRequest request) {
        ApiResponse response = summaryService.getDeliverySummary(request);
        return ResponseUtils.buildResponse(request, response);
    }
}