package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

@Entity
@Getter
@Setter
public class AddCartData extends MasterData {

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_size_id")
    private DishSize dishSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extra_id")
    private Extra extra;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderData order;

    public AddCartData() {}

    public AddCartData(final Integer quantity, final User customer, final DishSize dishSize, final Extra extra, final OrderData order) {
        this.quantity = quantity;
        this.customer = customer;
        this.dishSize = dishSize;
        this.extra = extra;
        this.order = order;
    }
}
