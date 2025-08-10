package org._p1m.foodorderingsystem.features.rating.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequestDTO {
    private Integer ratingPoints; // e.g., 1 to 5
    private Long userId; // The ID of the user giving the rating
    private Long deliveryUserId;
}
