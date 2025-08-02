package org._p1m.foodorderingsystem.features.addCart.dto.response;

import java.math.BigDecimal;

import org._p1m.foodorderingsystem.common.constant.Status;

/**
 * Represents the menu details in the cart response.
 *
 * @param id The ID of the menu.
 * @param name The name of the menu.
 * @param image The URL for the menu's image.
 * @param price The price for the specific menu selected.
 * @param status The status of the menu.
 */

public record MenuResponse(
		Long id,
        String name,
        String image,
        BigDecimal price,
        Status status
) {}
