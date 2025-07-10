package org._p1m.foodorderingsystem.features.users.repository;

import org._p1m.foodorderingsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDashboardRepository extends JpaRepository<User, Long> {
	Page<User> findAll(Pageable pageable);
}
