package org._p1m.foodorderingsystem.features.addCart.dto.response;

import java.math.BigDecimal;

/**
 * Represents an optional extra added to a cart item.
 *
 * @param id The ID of the extra.
 * @param name The name of the extra.
 * @param price The price of the extra.
 */
public record ExtraResponse(
        Long id,
        String name,
        BigDecimal price
) {}