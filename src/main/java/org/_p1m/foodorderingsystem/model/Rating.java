package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

@Entity
@Getter
@Setter
public class Rating extends MasterData {

    @Column(name = "rating_points", nullable = false)
    private Integer ratingPoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", nullable = false)
    private User deliveryUser;

    public Rating() {}

    public Rating(final Integer ratingPoints, final User user, final User deliveryUser) {
        this.ratingPoints = ratingPoints;
        this.user = user;
        this.deliveryUser = deliveryUser;
    }
}
