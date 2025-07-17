package org._p1m.foodorderingsystem.features.address.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.AddressEntityType;

import java.math.BigDecimal;

@Data
public class AddressUpdateRequestDto {
    @NotNull(message = "Address Region is required")
    private String region;
    @NotNull(message = "Address City is required")
    private String city;
    @NotNull(message = "Address Township is required")
    private String township;
    @NotNull(message = "Address Road is required")
    private String road;
    @NotNull(message = "Address Street is required")
    private String street;
    @NotNull(message = "Address Latitude is required")
    private BigDecimal lat;
    @NotNull(message = "Address Longitude is required")
    private BigDecimal longitude;
    @NotNull(message = "Address Entity Type is required")
    private AddressEntityType entityType;
    @NotNull(message = "Address Entity Id is required")
    private Long entityId;
}
