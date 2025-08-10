package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Coupon extends MasterData {

    @Column(name = "coupon_points", nullable = false, precision = 19, scale = 4)
    private BigDecimal couponPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Coupon() {}

    public Coupon(final BigDecimal couponPoints, final User user) {
        this.couponPoints = couponPoints;
        this.user = user;
    }
}
