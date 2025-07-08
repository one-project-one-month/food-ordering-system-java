package org._p1m.foodorderingsystem.features.restaurant_registration.dto.request;

import lombok.Data;

@Data
public class RestaurantUpdateRequest {
    private String restaurantName;
    private String contactNumber;
    private String restaurantImage;
    private String nrc;
    private String kpayNumber;
    private Long resOwnerId;
}
