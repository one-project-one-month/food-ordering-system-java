package org._p1m.foodorderingsystem.features.users.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.profile.dto.request.ProfileRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProfileService {

    public String uploadProfilePicture(final Long userId, final MultipartFile file);

    public ApiResponse createProfile(final Long userId,final ProfileRequestDto profileRequest,final MultipartFile file);

    public ApiResponse softDeleteProfile(final Long userId);

    public ApiResponse updateProfile(final Long userId,final ProfileRequestDto profileRequest);

    public ApiResponse getProfileById(final Long userId);




}
