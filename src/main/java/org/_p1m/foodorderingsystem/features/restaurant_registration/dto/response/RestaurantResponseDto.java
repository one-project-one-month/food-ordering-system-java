package org._p1m.foodorderingsystem.features.restaurant_registration.dto.response;

import lombok.Data;

@Data
public class RestaurantResponseDto {

    private String restaurantName;
    private String contactNumber;
    private String restaurantImage;
    private String nrc;
    private String kpayNumber;
    private Long resOwnerId;
}
