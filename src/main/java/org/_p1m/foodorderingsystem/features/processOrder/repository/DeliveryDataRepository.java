package org._p1m.foodorderingsystem.features.processOrder.repository;

import org._p1m.foodorderingsystem.common.constant.DeliveryStatus;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.DeliveryData;
import org._p1m.foodorderingsystem.model.RestaurantVendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryDataRepository extends JpaRepository<DeliveryData, Long> {
	@Query("""
		    SELECT dd FROM DeliveryData dd
		    JOIN dd.order od
		    WHERE dd.deliveryStaff.id = :deliveryId
		    AND od.deliveryStatus = :status
		""")
		Page<DeliveryData> findByDeliveryIdWithStatus(
		    @Param("deliveryId") Long deliveryId,
		    @Param("status") DeliveryStatus status,
		    Pageable pageable
		);
}
