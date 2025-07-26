package org._p1m.foodorderingsystem.features.processOrder.repository;

import org._p1m.foodorderingsystem.model.DeliveryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDataRepository extends JpaRepository<DeliveryData, Long> {
}
