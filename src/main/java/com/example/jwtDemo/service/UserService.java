package com.example.jwtDemo.service;

import com.example.jwtDemo.data.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserService {
    AppUser saveUser(AppUser appUser);
    Optional<AppUser> getUser(Long userId);
    List<AppUser> getAllUsers();

    AppUser updateUser(Long userId, AppUser appUser);

    void deleteUser(Long userId);
}
