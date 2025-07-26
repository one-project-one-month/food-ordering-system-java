package org._p1m.foodorderingsystem.features.order.repository;

import org._p1m.foodorderingsystem.model.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<OrderData, Long> {
    
}

