package org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response;

import lombok.Builder;
import lombok.Data;
import org._p1m.foodorderingsystem.common.constant.Status;

@Data
@Builder
public class SuperAdminDashBoardResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private Status status;
}
