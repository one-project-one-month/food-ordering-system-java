package org._p1m.foodorderingsystem.features.categories.repository;

import org._p1m.foodorderingsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByIdAndRestaurantId(Long categoryId,Long RestaurantId);
}
