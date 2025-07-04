package org._p1m.foodorderingsystem.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status implements BaseEnum<Integer>{
    ACTIVE(1),
    INACTIVE(2);

    private final int value;

    @Override
    public Integer getValue() {
        return this.value;
    }
}
