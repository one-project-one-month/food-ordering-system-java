package org._p1m.foodorderingsystem.summary.dto;

public record SummaryDeliveryResponse(
        long noOfDeliveredPerDay,
        long noOfDeliveredPerMonth,
        long noOfDeliveredPerYear,
        long noOfCancelledPerDay,
        long noOfCancelledPerMonth,
        long noOfCancelledPerYear
) {}
