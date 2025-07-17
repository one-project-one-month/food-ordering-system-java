package org._p1m.foodorderingsystem.features.users.repository;

import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long> {
	Page<User> findAll(Pageable pageable);

	User findByEmail(String email);
}
