package org._p1m.foodorderingsystem.features.users.repository;

import org._p1m.foodorderingsystem.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface UserTokenRepository extends JpaRepository<UserToken , Long> {

    UserToken findByUsername(String email);

    @Query(value = "SELECT * FROM user_tokens u WHERE u.username = :email ORDER BY u.createdAt DESC LIMIT 1;" , nativeQuery = true)
    UserToken findTopByUsernameOrderByCreatedAtDesc(@Param("email") String username);

    void deleteByUsername(String email);
}
