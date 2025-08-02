package org._p1m.foodorderingsystem.features.superadmin_manage_user.service.impl;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.constant.Status;
import org._p1m.foodorderingsystem.config.exceptions.EntityNotFoundException;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginatedApiResponse;
import org._p1m.foodorderingsystem.config.response.dto.PaginationMeta;
import org._p1m.foodorderingsystem.features.order.dto.response.OrderResponseDto;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.DeletedUserResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.dto.response.SuperAdminDashBoardResponse;
import org._p1m.foodorderingsystem.features.superadmin_manage_user.service.SuperAdminService;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.SuperAdminManageUserRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepo;
    private final ProfileRepository profileRepo;
    private final ModelMapper modelMapper;
    private final SuperAdminManageUserRepository superAdminManageUserRepo;


    @Override
    public ApiResponse deleteById(final Long id) {

        User user = userRepo.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("User id " + id + " is not Found"));

        user.delete();
        user.setEmail(null);
        User updatedUser = userRepo.save(user);

        DeletedUserResponse deletedUserResponse = modelMapper.map(updatedUser,DeletedUserResponse.class);

        deletedUserResponse.setDeletedAt(user.getDeletedAt().toString());
        deletedUserResponse.setRole(user.getRole().getName());

        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
                .data(Map.of("Deleted User",deletedUserResponse))
                .message("User is successfully deleted").build();
    }

    @Override
    public PaginatedApiResponse<SuperAdminDashBoardResponse> getAllUsersPaginated(
            String keyword, String role, String status, Pageable pageable) {

        Status dbStatus = status == null ? null : Status.valueOf(status);
        Page<User> profilePage = userRepo.findAll(pageable);

//        List<SuperAdminDashBoardResponse> userResponses = profilePage.getContent().stream().map(profile -> {
//            User user = profile.getProfile().getUser();
//            return SuperAdminDashBoardResponse.builder()
//                    .id(user.getId())
//                    .name(profile.get)
//                    .email(profile.getEmail())
//                    .phone(user.getProfile().getPhone())
//                    .address(user.getProfile().getAddress())
//                    .status(profile.getStatus())
//                    .build();
//        }).toList();


        List<SuperAdminDashBoardResponse> userResponses = profilePage.getContent().stream()
                .filter(user -> user.getProfile() != null)
                .map(user -> SuperAdminDashBoardResponse.builder()
                        .id(user.getId())
                        .name(user.getProfile().getName())
                        .email(user.getEmail())
                        .phone(user.getProfile().getPhone())
                        .address(user.getProfile().getAddress())
                        .status(user.getStatus())
                        .build()
                ).toList();

        PaginationMeta meta = new PaginationMeta();
        meta.setTotalItems(profilePage.getTotalElements());
        meta.setTotalPages(profilePage.getTotalPages());
        meta.setCurrentPage(pageable.getPageNumber()+1);
        return PaginatedApiResponse.<SuperAdminDashBoardResponse>builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Fetched successfully")
                .meta(meta)
                .data(userResponses)
                .build();
    }

}
