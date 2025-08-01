package org._p1m.foodorderingsystem.features.addCart.repository;

import java.util.List;

import org._p1m.foodorderingsystem.model.AddCartData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddCartMenuRepo extends JpaRepository<AddCartData,Long> {
	@Query("SELECT a FROM AddCartData a WHERE a.customer.id = :customerId AND a.order IS NULL")
	List<AddCartData> findUnorderedCartItemsByCustomerId(@Param("customerId") Long customerId);

}
