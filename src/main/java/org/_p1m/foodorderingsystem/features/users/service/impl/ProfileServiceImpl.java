package org._p1m.foodorderingsystem.features.users.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.storage.StorageService;
import org._p1m.foodorderingsystem.common.storage.StorageServiceFactory;
import org._p1m.foodorderingsystem.features.users.repository.ProfileRepository;
import org._p1m.foodorderingsystem.features.users.repository.RoleRepository;
import org._p1m.foodorderingsystem.features.users.repository.UserRepository;
import org._p1m.foodorderingsystem.features.users.service.ProfileService;
import org._p1m.foodorderingsystem.model.Profile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private  StorageService storageService;

    @Autowired
    public void setStorageService(StorageServiceFactory factory) {
        this.storageService = factory.getConfiguredStorageService();
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
