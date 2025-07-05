package org._p1m.foodorderingsystem.features.users.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.RoleRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.model.Profile;
import org._p1m.foodorderingsystem.model.Role;
import org._p1m.foodorderingsystem.model.User;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org._p1m.foodorderingsystem.features.users.request.UserCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final StorageService storageService;

    @Autowired
    public UserService(
            final UserRepository userRepository,
            final ProfileRepository profileRepository,
            final RoleRepository roleRepository,
            final StorageServiceFactory factory) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.roleRepository = roleRepository;
        this.storageService = factory.getConfiguredStorageService();
    }

    @Transactional
    public User createUser(UserCreateRequest request) {
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

        return this.userRepository.save(user);
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
