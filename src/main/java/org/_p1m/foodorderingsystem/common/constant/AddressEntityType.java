package org._p1m.foodorderingsystem.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AddressEntityType implements BaseEnum<Integer> {
    RESTAURANT(1),
    CUSTOMER(2);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
