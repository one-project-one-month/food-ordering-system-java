package org._p1m.foodorderingsystem.features.users.controller;

import org._p1m.foodorderingsystem.features.users.request.UserCreateRequest;
import org._p1m.foodorderingsystem.features.users.service.UserService;
import org._p1m.foodorderingsystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody final UserCreateRequest request) {
        final User createdUser = this.userService.createUser(request);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable final Long userId, @RequestParam("file") final MultipartFile file) {
        try {
            final String fileUrl = this.userService.uploadProfilePicture(userId, file);
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }
}
