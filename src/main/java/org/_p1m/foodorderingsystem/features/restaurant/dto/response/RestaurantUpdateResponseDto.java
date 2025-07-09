package org._p1m.foodorderingsystem.features.restaurant.dto.response;

import lombok.Data;

@Data
public class RestaurantUpdateResponseDto {
    private String restaurantName;
    private String contactNumber;
    private String restaurantImage;
    private String nrc;
    private String kpayNumber;
}
