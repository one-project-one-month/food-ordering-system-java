package org._p1m.foodorderingsystem.features.payment.repository;

import org._p1m.foodorderingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
