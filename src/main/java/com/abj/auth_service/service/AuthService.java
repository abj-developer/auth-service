package com.abj.auth_service.service;

import com.abj.auth_service.dto.LoginRequest;
import com.abj.auth_service.dto.LoginResponse;
import com.abj.auth_service.entity.AuthUser;
import com.abj.auth_service.entity.RefreshToken;
import com.abj.auth_service.repository.AuthUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse login(LoginRequest request) {

        AuthUser user = authUserRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid username or password"));

        if (!user.isEnabled()) {
            throw new BadCredentialsException("User is disabled");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new BadCredentialsException("Invalid username or password");
        }

        // 1. Generate short-lived access token
        String accessToken = jwtService.generateToken(user);

        // 2. Generate long-lived refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.getExpirationInSeconds()
        );
    }


    public LoginResponse refreshToken(String token) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(token);

        refreshTokenService.verifyExpiration(refreshToken);

        AuthUser user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.getExpirationInSeconds()
        );
    }
}