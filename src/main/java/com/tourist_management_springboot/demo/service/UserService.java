package com.tourist_management_springboot.demo.service;

import com.tourist_management_springboot.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void changePassword(Long userId, String oldPassword, String newPassword);
    void updateUserRole(Long userId, String roleName);
}
