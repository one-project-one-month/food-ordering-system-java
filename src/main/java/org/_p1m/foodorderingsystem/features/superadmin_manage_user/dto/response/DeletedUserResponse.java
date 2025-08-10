package org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeletedUserResponse {
    private Long id;
    private String email;
    private String role;
    private String status;
    private String deletedAt;
}
