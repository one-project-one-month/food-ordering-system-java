package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

@Entity
@Getter
@Setter
public class Category extends MasterData {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_id", nullable = false)
    private Restaurant restaurant;

    public Category() {}

    public Category(String name, Restaurant restaurant) {
        this.name = name;
        this.restaurant = restaurant;
    }
}
