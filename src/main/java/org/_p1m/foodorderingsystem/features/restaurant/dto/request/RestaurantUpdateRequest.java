package org._p1m.foodorderingsystem.features.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantUpdateRequest {
    @NotBlank(message = "Restaurant name is required")
    private String restaurantName;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Restaurant image is required")
    private String restaurantImage;

    @NotBlank(message = "NRC is required")
    private String nrc;

    @NotBlank(message = "KPay number is required")
    private String kpayNumber;

}
