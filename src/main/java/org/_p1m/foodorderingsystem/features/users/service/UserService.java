package org._p1m.foodorderingsystem.features.users.service;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org._p1m.foodorderingsystem.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    
    public ApiResponse createUser(UserCreateRequest request);

    public String uploadProfilePicture(final Long userId, final MultipartFile file);

    public String varifiedUser(User user);

    public ApiResponse verifyAccount(long code , String email);

    public ApiResponse resendCode(String email);
}
