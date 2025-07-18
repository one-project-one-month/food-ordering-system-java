package org._p1m.foodorderingsystem.features.order.repository;

import org._p1m.foodorderingsystem.model.OrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDataRepository extends JpaRepository<OrderData,Long> {
}
