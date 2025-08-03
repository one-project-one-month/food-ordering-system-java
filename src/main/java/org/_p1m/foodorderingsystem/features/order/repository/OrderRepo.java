package org._p1m.foodorderingsystem.features.order.repository;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.model.OrderData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepo extends JpaRepository<OrderData, Long> {
	@Query("""
		    SELECT o FROM OrderData o
		    JOIN o.addCartItems ac
		    JOIN ac.dishSize ds
		    JOIN ds.menu m
		    JOIN m.restaurant r
		    WHERE r.id = :restaurantId
		    AND o.deliveryStatus = :status
		""")
		Page<OrderData> findOrdersByRestaurantIdWithStatus(@Param("restaurantId") Long restaurantId, Pageable pageable,
				@Param("status") DeliveryStatus status);
		
	@Query("""
		    SELECT DISTINCT o FROM OrderData o
		    JOIN o.addCartItems ac
		    JOIN ac.customer c
		    WHERE c.id = :customerId
		    AND o.deliveryStatus = :status
		""")
		Page<OrderData> findOrdersByCustomerIdWithStatus(@Param("customerId") Long customerId, Pageable pageable,
				@Param("status") DeliveryStatus status);
}

