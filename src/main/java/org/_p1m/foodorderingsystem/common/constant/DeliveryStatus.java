package org._p1m.foodorderingsystem.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus implements BaseEnum<Integer> {
    PENDING(1),
    ACCEPTED(2),
    ON_THE_WAY(3),
    DELIVERED(4),
    CANCELLED(5);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
