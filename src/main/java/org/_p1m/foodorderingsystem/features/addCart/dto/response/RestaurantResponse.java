package org._p1m.foodorderingsystem.features.addCart.dto.response;

/**
 * Represents the restaurant details.
 *
 * @param id The ID of the restaurant.
 * @param name The name of the restaurant.
 */
public record RestaurantResponse(
        Long id,
        String name
) {}