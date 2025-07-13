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
@Table(name = "users")
public class User extends MasterData {
    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Profile profile;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Coupon> coupons = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Rating> givenRatings = new HashSet<>();

    @OneToMany(
            mappedBy = "deliveryUser",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Rating> receivedRatings = new HashSet<>();

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Restaurant> restaurants = new HashSet<>();

    @OneToMany(mappedBy = "deliveryUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RestaurantVendor> vendorAssignments = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AddCartData> cartData = new HashSet<>();

    public User() {}

    public User(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public void addRestaurant(final Restaurant restaurant) {
        this.restaurants.add(restaurant);
        restaurant.setOwner(this);
    }

    public void removeRestaurant(final Restaurant restaurant) {
        this.restaurants.remove(restaurant);
        restaurant.setOwner(null);
    }

    public void setProfile(final Profile profile) {
        if (profile == null) {
            if (this.profile != null) {
                this.profile.setUser(null);
            }
        } else {
            profile.setUser(this);
        }
        this.profile = profile;
    }

    public void addCoupon(final Coupon coupon) {
        this.coupons.add(coupon);
        coupon.setUser(this);
    }

    public void removeCoupon(final Coupon coupon) {
        this.coupons.remove(coupon);
        coupon.setUser(null);
    }

    public void giveRating(final Rating rating) {
        this.givenRatings.add(rating);
        rating.setDeliveryUser(this);
    }

    public void removeGivenRating(final Rating rating) {
        this.givenRatings.remove(rating);
        rating.setDeliveryUser(null);
    }

    public void receiveRating(final Rating rating) {
        this.receivedRatings.add(rating);
        rating.setDeliveryUser(this);
    }

    public void removeReceivedRating(final Rating rating) {
        this.receivedRatings.remove(rating);
        rating.setDeliveryUser(null);
    }

    public void addVendorAssignment(RestaurantVendor vendorAssignment) {
        this.vendorAssignments.add(vendorAssignment);
        vendorAssignment.setDeliveryUser(this);
    }

    public void removeVendorAssignment(RestaurantVendor vendorAssignment) {
        this.vendorAssignments.remove(vendorAssignment);
        vendorAssignment.setDeliveryUser(null);
    }
}
