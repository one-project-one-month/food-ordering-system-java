package org._p1m.foodorderingsystem.features.users.repository;

import org._p1m.foodorderingsystem.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserTokenRepository extends JpaRepository<UserToken , Long> {

}
