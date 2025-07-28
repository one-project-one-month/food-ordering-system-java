package org._p1m.foodorderingsystem.features.processOrder.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String address;
}
