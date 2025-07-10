package org._p1m.foodorderingsystem.features.rating.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingResponseDTO {
    private Long id;
    private Integer ratingPoints;
    private Long userId;
    private Long deliveryUserId;
}
