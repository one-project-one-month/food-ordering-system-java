package org._p1m.foodorderingsystem.features.addCart.repository;

import org._p1m.foodorderingsystem.model.AddCartData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddCartMenuRepo extends JpaRepository<AddCartData,Long> {
}
