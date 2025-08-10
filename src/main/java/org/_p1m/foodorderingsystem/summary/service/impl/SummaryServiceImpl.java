package org._p1m.foodorderingsystem.summary.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.util.JWTUtil;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.order.repository.OrderDataRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.OrderData;
import org._p1m.foodorderingsystem.model.User;
import org._p1m.foodorderingsystem.summary.dto.SummaryDeliveryResponse;
import org._p1m.foodorderingsystem.summary.dto.SummaryOwnerResponse;
import org._p1m.foodorderingsystem.summary.service.SummaryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final OrderDataRepository orderDataRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public SummaryServiceImpl(OrderDataRepository orderDataRepository, JWTUtil jwtUtil, UserRepository userRepository) {
        this.orderDataRepository = orderDataRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse getOwnerSummary(HttpServletRequest request) {
        final User currentUser = getCurrentUser(request);
        if (currentUser == null || !("RESTAURANT_OWNER".equals(currentUser.getRole().getName()))) {
            return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value()).message("Unauthorized").build();
        }

        final List<OrderData> allOrders = orderDataRepository.findAll().stream()
                .filter(order -> order.getPayment() != null && order.getPayment().getRestaurant() != null &&
                        order.getPayment().getRestaurant().getOwner().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());

        final TimeRanges timeRanges = new TimeRanges();

        final long noOfOrderPerDay = getOrderCountInDateRange(allOrders, timeRanges.startOfDay, timeRanges.endOfDay, order -> true);
        final long noOfOrderPerMonth = getOrderCountInDateRange(allOrders, timeRanges.startOfMonth, timeRanges.endOfMonth, order -> true);
        final long noOfOrderPerYear = getOrderCountInDateRange(allOrders, timeRanges.startOfYear, timeRanges.endOfYear, order -> true);

        final BigDecimal amountOfTotalOrderPerDay = getTotalAmountInDateRange(allOrders, timeRanges.startOfDay, timeRanges.endOfDay);
        final BigDecimal amountOfTotalOrderPerMonth = getTotalAmountInDateRange(allOrders, timeRanges.startOfMonth, timeRanges.endOfMonth);
        final BigDecimal amountOfTotalOrderPerYear = getTotalAmountInDateRange(allOrders, timeRanges.startOfYear, timeRanges.endOfYear);

        SummaryOwnerResponse summary = new SummaryOwnerResponse(noOfOrderPerDay, noOfOrderPerMonth, noOfOrderPerYear, amountOfTotalOrderPerDay, amountOfTotalOrderPerMonth, amountOfTotalOrderPerYear);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(Map.of("summary", summary)).message("Owner summary retrieved successfully.").build();
    }

    @Override
    public ApiResponse getDeliverySummary(HttpServletRequest request) {
        final User currentUser = getCurrentUser(request);
        if (currentUser == null || !("DELIVERY_STUFF".equals(currentUser.getRole().getName()))) {
            return ApiResponse.builder().success(0).code(HttpStatus.UNAUTHORIZED.value()).message("Unauthorized").build();
        }

        final List<OrderData> allOrders = orderDataRepository.findAll().stream()
                .filter(order -> order.getDeliveryData() != null && order.getDeliveryData().getDeliveryStaff().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());

        final TimeRanges timeRanges = new TimeRanges();

        final Predicate<OrderData> isDelivered = order -> order.getDeliveryStatus() == DeliveryStatus.DELIVERED;
        final Predicate<OrderData> isCancelled = order -> order.getDeliveryStatus() == DeliveryStatus.CANCELLED;

        final long noOfDeliveredPerDay = getOrderCountInDateRange(allOrders, timeRanges.startOfDay, timeRanges.endOfDay, isDelivered);
        final long noOfDeliveredPerMonth = getOrderCountInDateRange(allOrders, timeRanges.startOfMonth, timeRanges.endOfMonth, isDelivered);
        final long noOfDeliveredPerYear = getOrderCountInDateRange(allOrders, timeRanges.startOfYear, timeRanges.endOfYear, isDelivered);

        final long noOfCancelledPerDay = getOrderCountInDateRange(allOrders, timeRanges.startOfDay, timeRanges.endOfDay, isCancelled);
        final long noOfCancelledPerMonth = getOrderCountInDateRange(allOrders, timeRanges.startOfMonth, timeRanges.endOfMonth, isCancelled);
        final long noOfCancelledPerYear = getOrderCountInDateRange(allOrders, timeRanges.startOfYear, timeRanges.endOfYear, isCancelled);

        SummaryDeliveryResponse summary = new SummaryDeliveryResponse(noOfDeliveredPerDay, noOfDeliveredPerMonth, noOfDeliveredPerYear, noOfCancelledPerDay, noOfCancelledPerMonth, noOfCancelledPerYear);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value()).data(Map.of("summary", summary)).message("Delivery summary retrieved successfully.").build();
    }

    private User getCurrentUser(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromRequest(request);
        if (token == null) {
            return null;
        }
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email);
    }

    private long getOrderCountInDateRange(List<OrderData> orders, LocalDateTime start, LocalDateTime end, Predicate<OrderData> additionalPredicate) {
        return orders.stream()
                .filter(order -> !order.getCreatedAt().isBefore(start) && !order.getCreatedAt().isAfter(end))
                .filter(additionalPredicate)
                .count();
    }

    private BigDecimal getTotalAmountInDateRange(List<OrderData> orders, LocalDateTime start, LocalDateTime end) {
        return orders.stream()
                .filter(order -> !order.getCreatedAt().isBefore(start) && !order.getCreatedAt().isAfter(end))
                .map(OrderData::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static class TimeRanges {
        final LocalDateTime startOfDay;
        final LocalDateTime endOfDay;
        final LocalDateTime startOfMonth;
        final LocalDateTime endOfMonth;
        final LocalDateTime startOfYear;
        final LocalDateTime endOfYear;

        TimeRanges() {
            LocalDate today = LocalDate.now();
            this.startOfDay = today.atStartOfDay();
            this.endOfDay = today.atTime(LocalTime.MAX);
            this.startOfMonth = today.withDayOfMonth(1).atStartOfDay();
            this.endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX);
            this.startOfYear = today.withDayOfYear(1).atStartOfDay();
            this.endOfYear = today.withDayOfYear(today.lengthOfYear()).atTime(LocalTime.MAX);
        }
    }
}