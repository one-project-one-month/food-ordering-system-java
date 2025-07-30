package org._p1m.foodorderingsystem.features.addCart.dto.response;

import java.math.BigDecimal;

/**
 * Represents the dish details in the cart response.
 *
 * @param id The ID of the menu item (dish).
 * @param name The name of the dish.
 * @param image The URL for the dish's image.
 * @param price The price for the specific dish size selected.
 * @param restaurant The details of the restaurant providing the dish.
 */
public record DishResponse(
        Long id,
        String name,
        String image,
        BigDecimal price,
        RestaurantResponse restaurant
) {}