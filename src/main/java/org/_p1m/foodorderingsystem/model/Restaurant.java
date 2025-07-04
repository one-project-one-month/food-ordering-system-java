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

    @OneToMany(
            mappedBy = "restaurant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Menu> menus = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantVendor> restaurantVendors = new HashSet<>();

    public Restaurant() {}

    public Restaurant(final String restaurantName, final String contactNumber, final String restaurantImage, final String nrc, final String kpayNumber, final User owner) {
        this.restaurantName = restaurantName;
        this.contactNumber = contactNumber;
        this.restaurantImage = restaurantImage;
        this.nrc = nrc;
        this.kpayNumber = kpayNumber;
        this.owner = owner;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.setRestaurant(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.setRestaurant(null);
    }

    public void addMenu(Menu menu) {
        this.menus.add(menu);
        menu.setRestaurant(this);
    }

    public void removeMenu(Menu menu) {
        this.menus.remove(menu);
        menu.setRestaurant(null);
    }

    public void addVendor(RestaurantVendor vendor) {
        this.restaurantVendors.add(vendor);
        vendor.setRestaurant(this);
    }

    public void removeVendor(RestaurantVendor vendor) {
        this.restaurantVendors.remove(vendor);
        vendor.setRestaurant(null);
    }
}
