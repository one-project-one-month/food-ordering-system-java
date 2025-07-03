package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Menu extends MasterData {

    @Column(nullable = false)
    private String dish;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "dish_img")
    private String dishImg;

    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DishSize> dishSizes = new HashSet<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Extra> extras = new HashSet<>();

    public Menu() {}

    public void addDishSize(final DishSize dishSize) {
        dishSizes.add(dishSize);
        dishSize.setMenu(this);
    }

    public void removeDishSize(final DishSize dishSize) {
        dishSizes.remove(dishSize);
        dishSize.setMenu(null);
    }

    public void addExtra(final Extra extra) {
        extras.add(extra);
        extra.setMenu(this);
    }

    public void removeExtra(final Extra extra) {
        extras.remove(extra);
        extra.setMenu(null);
    }
}
