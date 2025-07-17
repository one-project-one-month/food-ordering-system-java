package org._p1m.foodorderingsystem.features.restaurant_vendors.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org._p1m.foodorderingsystem.model.RestaurantVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org._p1m.foodorderingsystem.common.constant.Status;

import java.util.List;
import java.util.Optional;


@Repository
public interface RestaurantVendorRepository extends JpaRepository<RestaurantVendor, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE RestaurantVendor  d SET d.status = :status WHERE d.restaurant.id = :restaurantId AND d.deliveryUser.id = :deliveryId")
    void updateDeliveryStatus(@Param("restaurantId") Long restaurantId,
                                             @Param("deliveryId") Long deliveryId,
                                             @Param("status") Status status);

    Optional<RestaurantVendor> findByRestaurantIdAndDeliveryUserId(@NotNull(message = "Restaurant Id is required") Long restaurantId, @NotNull(message = "Delivery Id is required") Long deliveryId);

    List<RestaurantVendor> findByRestaurantId(Long restaurantId);
}
