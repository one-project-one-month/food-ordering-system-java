package org._p1m.foodorderingsystem.features.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantCreateRequest {
    @NotBlank(message = "Restaurant name is required")
    private String restaurantName;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "NRC is required")
    private String nrc;

    @NotBlank(message = "KPay number is required")
    private String kpayNumber;

    @NotNull(message = "Restaurant owner ID is required")
    private Long resOwnerId;
}
