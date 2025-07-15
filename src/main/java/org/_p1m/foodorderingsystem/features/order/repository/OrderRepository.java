package org._p1m.foodorderingsystem.features.order.repository;

import org._p1m.foodorderingsystem.model.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderData, Long> {
    List<OrderData> findByRestaurantId(Long restaurantId);

}
