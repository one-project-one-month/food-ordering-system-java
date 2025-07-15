package org._p1m.foodorderingsystem.features.payment.repository;

import org._p1m.foodorderingsystem.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentData, Long> {
}
