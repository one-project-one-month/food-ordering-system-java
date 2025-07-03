package org._p1m.foodorderingsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org._p1m.foodorderingsystem.common.entity.MasterData;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Restaurant extends MasterData {

    @Column(name = "restaurant_name", nullable = false)
    private String restaurantName;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "restaurant_image")
    private String restaurantImage;

    @Column(name = "nrc", unique = true)
    private String nrc;

    @Column(name = "kpay_number", unique = true)
    private String kpayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "res_owner_id", nullable = false)
    private User owner;

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Category> categories = new HashSet<>();

    public Restaurant() {}

    public void addCategory(Category category) {
        categories.add(category);
        category.setRestaurant(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.setRestaurant(null);
    }
}
