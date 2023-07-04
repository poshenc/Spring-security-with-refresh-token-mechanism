package com.example.jwtDemo.controller;

import com.example.jwtDemo.DTO.UserResDTO;
import com.example.jwtDemo.data.AppUser;
import com.example.jwtDemo.data.RefreshToken;
import com.example.jwtDemo.security.RefreshTokenService;
import com.example.jwtDemo.security.JwtProvider;
import com.example.jwtDemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResDTO>>getAllUsers() {
        System.out.println("fetch all users");
        return ResponseEntity.ok().body(userService.getAllUsers().stream().map(UserResDTO::new).collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResDTO>getUserByUserName(@PathVariable Long userId) {
        return ResponseEntity.ok().body(new UserResDTO(userService.getUser(userId).get()));
    }

    @PostMapping("/user/register")
    public ResponseEntity<AppUser>saveUser(@RequestBody AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(appUser));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<AppUser>updateUser(@PathVariable Long userId, @RequestBody AppUser appUser) {
        return ResponseEntity.ok().body(userService.updateUser(userId, appUser));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?>deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<Map<String, String>>refreshToken(@RequestBody String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAppUser)
                .map(appUser -> {
                    String newAccessToken = JwtProvider.generateToken(appUser.getUserName());
                    Map<String, String> tokensResponse = new HashMap<>();
                    tokensResponse.put("accessToken", newAccessToken);
                    tokensResponse.put("refreshToken", refreshToken);
                    return ResponseEntity.ok().body(tokensResponse);
                }).orElseThrow(() -> {
                    throw new RuntimeException("Refresh token is not in database!");
                });
    }

}
