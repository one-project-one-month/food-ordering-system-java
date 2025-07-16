package org._p1m.foodorderingsystem.features.address.repository;


import org._p1m.foodorderingsystem.common.constant.AddressEntityType;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.entityType = :entityType AND a.entityId = :entityId")
    Address findByEntityTypeAndEntityId(AddressEntityType entityType, Long entityId);


    Optional<Address> findByIdAndStatus(Long id, Status status);
}
