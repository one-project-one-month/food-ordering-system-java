package org._p1m.foodorderingsystem.features.processOrder.repository;

import org._p1m.foodorderingsystem.model.RestaurantVendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantVendorRepository extends JpaRepository<RestaurantVendor, Long> {
}
