package org._p1m.foodorderingsystem.features.users.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ProfileService {

    public String uploadProfilePicture(final Long userId, final MultipartFile file);
}
