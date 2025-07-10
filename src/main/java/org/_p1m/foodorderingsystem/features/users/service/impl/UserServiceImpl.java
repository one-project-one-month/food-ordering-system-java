package org._p1m.foodorderingsystem.features.users.service.impl;

import java.util.Map;

import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.users.dto.request.UserCreateRequest;
import org._p1m.foodorderingsystem.features.users.dto.response.UserResponseDto;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.RoleRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.features.users.service.UserService;
import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.Role;
import org._p1m.foodorderingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private StorageService storageService;



    @Autowired
    public void setStorageService(StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
    }
    
    @Transactional
    public ApiResponse createUser(UserCreateRequest request) {
        final Role defaultRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new EntityNotFoundException("Default role 'CUSTOMER' not found."));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(defaultRole);

        Profile profile = new Profile();
        profile.setName(request.getName());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());

        user.setProfile(profile);

        userRepository.save(user);
        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);
        return ApiResponse.builder().success(1).code(HttpStatus.OK.value())
		.data(Map.of("currentUser", dto))
		.message("User account created.").build();
    }

    @Transactional
    public String uploadProfilePicture(final Long userId, final MultipartFile file) {
        final Profile profile = this.profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found for user ID: " + userId));

        final String filename = storageService.store(file);

        final String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();

        profile.setProfilePic(fileUrl);
        this.profileRepository.save(profile);

        return fileUrl;
    }
}
