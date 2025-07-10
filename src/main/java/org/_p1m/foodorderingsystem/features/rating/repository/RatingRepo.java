package org._p1m.foodorderingsystem.features.rating.repository;

import org._p1m.foodorderingsystem.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
   public interface RatingRepo extends JpaRepository<Rating, Long> {
   }
