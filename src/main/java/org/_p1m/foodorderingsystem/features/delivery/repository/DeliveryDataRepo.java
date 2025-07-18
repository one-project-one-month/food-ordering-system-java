package org._p1m.foodorderingsystem.features.delivery.repository;

import org._p1m.foodorderingsystem.model.DeliveryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryDataRepo extends JpaRepository<DeliveryData ,Long> {

}
