package org.expensetracker.auth.controller;

import org.expensetracker.auth.entity.RefreshToken;
import org.expensetracker.auth.request.AuthRequest;
import org.expensetracker.auth.request.RefreshTokenRequest;
import org.expensetracker.auth.response.JwtResponse;
import org.expensetracker.auth.security.JwtService;
import org.expensetracker.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class TokenController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(request.getUsername());


        return ResponseEntity.ok(
                JwtResponse.builder()
                        .accessToken(jwtService.generateToken(userDetails)) //roles included
                        .refreshToken(refreshToken.getToken())
                        .build()
        );

    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody RefreshTokenRequest request) {

        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(user -> JwtResponse.builder()
                        .accessToken(jwtService.generateToken(user.getUsername()))
                        .refreshToken(request.getRefreshToken())
                        .build())
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));
    }
}
