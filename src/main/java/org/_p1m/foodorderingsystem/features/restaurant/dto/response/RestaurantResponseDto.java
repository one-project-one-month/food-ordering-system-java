package org._p1m.foodorderingsystem.features.restaurant.dto.response;

import lombok.Data;

@Data
public class RestaurantResponseDto {
    private Long id;
    private String restaurantName;
    private String contactNumber;
    private String restaurantImage;
    private String nrc;
    private String kpayNumber;
    private String createdAt;
    private String updatedAt;
    private Long resOwnerId;
}
