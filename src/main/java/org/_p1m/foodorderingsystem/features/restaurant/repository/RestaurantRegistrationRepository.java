package org._p1m.foodorderingsystem.features.restaurant.repository;

import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRegistrationRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByOwnerId(Long resOwnerId);

    Optional<Restaurant> findByIdAndStatus(Long id, Status status);
}
