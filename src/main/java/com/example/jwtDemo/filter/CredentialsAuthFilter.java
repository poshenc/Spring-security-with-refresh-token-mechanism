package com.example.jwtDemo.filter;

import com.example.jwtDemo.data.AppUser;
import com.example.jwtDemo.data.RefreshToken;
import com.example.jwtDemo.security.RefreshTokenService;
import com.example.jwtDemo.security.JwtProvider;
import com.example.jwtDemo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CredentialsAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public CredentialsAuthFilter(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    //login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(userName);
        System.out.println(password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String accessToken = JwtProvider.generateToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
//        String refreshToken = JwtService.refreshToken(user.getUsername());
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userName", user.getUsername());
        userInfo.put("accessToken", accessToken);
        userInfo.put("refreshToken", refreshToken.getToken());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), userInfo);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", failed.getMessage());
        response.setContentType("application/json");
        response.setStatus(401);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
