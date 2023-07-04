package com.example.jwtDemo.service;

import com.example.jwtDemo.DTO.UserResDTO;
import com.example.jwtDemo.data.AppUser;
import com.example.jwtDemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUser appUser) {
        if(appUser.getPassword() != null) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        }
        return userRepository.save(appUser);
    }


    @Override
    public AppUser updateUser(Long userId, AppUser appUser) {
        System.out.println("updating ************");
        System.out.println("name ************" + appUser.getName());
        System.out.println("username ************" + appUser.getUserName());
        System.out.println("password ************" + appUser.getPassword());
        AppUser foundUser =  getUser(userId).get();
        if(appUser.getName() != null) {
            System.out.println("1 passed");
            foundUser.setName(appUser.getName());
        }
        if(appUser.getUserName() != null) {
            System.out.println("2 passed");
            foundUser.setUserName(appUser.getUserName());
        }
        if(appUser.getPassword() != null && !appUser.getPassword().equals("")) {
            System.out.println("3 passed");
            foundUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        }
        System.out.println("finalliseee***" + foundUser.toString());
        return userRepository.save(foundUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Optional<AppUser> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }


}
