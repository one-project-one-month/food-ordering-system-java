package org._p1m.foodorderingsystem.features.superadmin_manage_user.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.repsitory.ProfileRepsitory;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.repsitory.UserRepository;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.service.SuperAdminService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepo;
    private final ProfileRepsitory profileRepo;


    @Override
    public ApiResponse deleteById(Long id) {
        return null;
    }
}
