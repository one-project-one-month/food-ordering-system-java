package org._p1m.foodorderingsystem.features.users.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.SuperAdminDashBoardResponse;
import org._p1m.foodorderingsystem.model.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperAdminManageUserRepository extends JpaRepository<Profile,Long> {


    @Query("""
        SELECT new org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.SuperAdminDashBoardResponse(
            p.id, p.name, p.email, p.phone, p.address, u.role.name, u.status
        )
        FROM Profile p
        JOIN p.user u 
        WHERE 
            (:keyword IS NULL OR
             LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(p.nrc) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(p.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:role IS NULL OR LOWER(u.role.name) = LOWER(:role))
        AND (:status IS NULL OR u.status = :status)
        """)
    Page<SuperAdminDashBoardResponse> searchUsers(
            @Param("keyword") String keyword,
            @Param("role") String role,
            @Param("status") Status status,
            Pageable pageable
    );

}
