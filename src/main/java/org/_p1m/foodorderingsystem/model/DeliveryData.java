package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

@Entity
@Getter
@Setter
public class DeliveryData extends MasterData {

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderData order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_stuff_id", nullable = false)
    private User deliveryStaff;

    public DeliveryData() {}

    public DeliveryData(final OrderData order, final User deliveryStaff) {
        this.order = order;
        this.deliveryStaff = deliveryStaff;
    }
}
