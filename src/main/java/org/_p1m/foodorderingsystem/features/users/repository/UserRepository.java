package org._p1m.foodorderingsystem.features.users.repository;

import org._p1m.foodorderingsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
