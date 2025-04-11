package com.tourist_management_springboot.demo.controller;


import com.tourist_management_springboot.demo.model.User;
import com.tourist_management_springboot.demo.repository.UserRepository;
import com.tourist_management_springboot.demo.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('GUIDE')")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findById(userDetails.getId());

        return userOptional.map(user -> {
            // Remove password before returning
            user.setPassword("");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('GUIDE')")
    public ResponseEntity<User> updateCurrentUser(@RequestBody User userUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findById(userDetails.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update fields that are allowed to be updated
            if (userUpdate.getFullName() != null) {
                user.setFullName(userUpdate.getFullName());
            }
            if (userUpdate.getPhoneNumber() != null) {
                user.setPhoneNumber(userUpdate.getPhoneNumber());
            }

            // Don't update username or email as they need to be unique

            User updatedUser = userRepository.save(user);
            // Remove password before returning
            updatedUser.setPassword("");
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('GUIDE')")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData) {
        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userOptional = userRepository.findById(userDetails.getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if old password matches
            if (!encoder.matches(oldPassword, user.getPassword())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Current password is incorrect");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Update password
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
