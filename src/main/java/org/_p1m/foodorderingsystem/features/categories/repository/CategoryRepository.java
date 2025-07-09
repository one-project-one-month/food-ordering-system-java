package org._p1m.foodorderingsystem.features.categories.repository;

import org._p1m.foodorderingsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

}
