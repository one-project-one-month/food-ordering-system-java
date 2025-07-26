package org._p1m.foodorderingsystem.features.order.repository;

import org._p1m.foodorderingsystem.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<PaymentData, Long> {
    
}
