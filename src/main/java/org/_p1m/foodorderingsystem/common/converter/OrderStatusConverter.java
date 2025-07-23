package org._p1m.foodorderingsystem.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org._p1m.foodorderingsystem.common.constant.BaseEnum;
import org._p1m.foodorderingsystem.common.constant.OrderStatus;
import org.hibernate.query.Order;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        if(attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return BaseEnum.fromValue(OrderStatus.class, dbData);
    }
}