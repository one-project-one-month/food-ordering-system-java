//package org._p1m.foodorderingsystem.features.Menu.repository;
//
//public interface DishSizeRepo {
//}
package org._p1m.foodorderingsystem.features.Menu.repository;

import org._p1m.foodorderingsystem.model.DishSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishSizeRepo extends JpaRepository<DishSize, Long> {
}
