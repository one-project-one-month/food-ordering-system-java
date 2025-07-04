package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class DishSize extends MasterData {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "dish_size_img")
    private String dishSizeImg;

    public DishSize() {}

    public DishSize(final String name, final BigDecimal price, final Menu menu, final String dishSizeImg) {
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.dishSizeImg = dishSizeImg;
    }
}
