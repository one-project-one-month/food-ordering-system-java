package org._p1m.foodorderingsystem.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org._p1m.foodorderingsystem.common.constant.BaseEnum;
import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;

@Converter(autoApply = true)
public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DeliveryStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public DeliveryStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return BaseEnum.fromValue(DeliveryStatus.class, dbData);
    }
}
