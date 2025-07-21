package org._p1m.foodorderingsystem.features.restaurant.repository;

import java.util.List;

import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	List<Restaurant> findByOwnerIdAndStatus(Long ownerId, Status status);
}
