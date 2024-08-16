package com.example.cinemaCore.controllers;

import com.example.cinemaCore.auth.entities.RefreshToken;
import com.example.cinemaCore.auth.entities.User;
import com.example.cinemaCore.auth.services.AuthService;
import com.example.cinemaCore.auth.services.JwtService;
import com.example.cinemaCore.auth.services.RefreshTokenService;
import com.example.cinemaCore.auth.utils.AuthRespone;
import com.example.cinemaCore.auth.utils.LoginRequest;
import com.example.cinemaCore.auth.utils.RefreshTokenRequest;
import com.example.cinemaCore.auth.utils.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthRespone> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthRespone> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthRespone> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthRespone.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshTokenRequest.getRefreshToken())
                        .build());
    }
}
