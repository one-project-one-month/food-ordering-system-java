package org._p1m.foodorderingsystem.features.superadmin_manage_user.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.DeletedUserResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.service.SuperAdminService;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;
    private final ModelMapper modelMapper;


    @Override
    public ApiResponse deleteById(final Long id) {

        User user = userRepo.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("User id " + id + " is not Found"));

        String userName = user.getProfile().getName();

        user.setStatus(Status.INACTIVE);
        user.delete();
        User updatedUser = userRepo.save(user);

        DeletedUserResponse deletedUserResponse = modelMapper.map(user,DeletedUserResponse.class);

        deletedUserResponse.setName(userName);
        deletedUserResponse.setDeletedAt(user.getDeletedAt().toString());

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("Deleted User",deletedUserResponse))
                .message("User is successfully deleted").build();
    }
}
