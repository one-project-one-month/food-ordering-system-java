package org._p1m.foodorderingsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.AddressEntityType;
import org._p1m.foodorderingsystem.common.converter.AddressEntityTypeConverter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Address extends MasterData {

    private String region;

    private String city;

    private String township;

    private String road;

    private String street;

    @Column(precision = 10, scale = 8)
    private BigDecimal lat;

    @Column(name = "`long`", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "entity_type")
    @Convert(converter = AddressEntityTypeConverter.class)
    private AddressEntityType entityType;

    @Column(name = "entity_id")
    private Long entityId;

    public Address() {}

    public Address(final String region, final String city, final String township, final String road, final String street, final BigDecimal lat, final BigDecimal longitude, final AddressEntityType entityType, final Long entityId) {
        this.region = region;
        this.city = city;
        this.township = township;
        this.road = road;
        this.street = street;
        this.lat = lat;
        this.longitude = longitude;
        this.entityType = entityType;
        this.entityId = entityId;
    }
}
