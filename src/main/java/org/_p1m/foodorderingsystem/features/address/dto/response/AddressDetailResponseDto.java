package org._p1m.foodorderingsystem.features.address.dto.response;

import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.AddressEntityType;

import java.math.BigDecimal;
@Data
public class AddressDetailResponseDto{
    private String region;

    private String city;

    private String township;

    private String road;

    private String street;

    private BigDecimal lat;

    private BigDecimal longitude;

    private AddressEntityType entityType;

    private Long entityId;

    private Long addressId;
}
