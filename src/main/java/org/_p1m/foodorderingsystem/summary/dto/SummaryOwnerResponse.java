package org._p1m.foodorderingsystem.summary.dto;

import java.math.BigDecimal;

public record SummaryOwnerResponse(
        long noOfOrderPerDay,
        long noOfOrderPerMonth,
        long noOfOrderPerYear,
        BigDecimal amountOfTotalOrderPerDay,
        BigDecimal amountOfTotalOrderPerMonth,
        BigDecimal amountOfTotalOrderPerYear
) {}
