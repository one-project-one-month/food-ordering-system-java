package org._p1m.foodorderingsystem.common.converter;

import org._p1m.foodorderingsystem.common.constant.Status;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter extends BaseEnumConverter<Status, Integer>{

     public StatusConverter() {
        super(Status.class);
    }
}