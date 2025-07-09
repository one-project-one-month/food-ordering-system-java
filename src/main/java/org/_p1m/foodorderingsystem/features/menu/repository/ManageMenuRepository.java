package org._p1m.foodorderingsystem.features.menu.repository;

import org._p1m.foodorderingsystem.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManageMenuRepository extends JpaRepository<Menu,Long> {
}
