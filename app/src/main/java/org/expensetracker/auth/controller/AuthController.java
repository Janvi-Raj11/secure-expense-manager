package org.expensetracker.auth.controller;

import org.expensetracker.auth.dto.UserInfoDto;
import org.expensetracker.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/v1")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserInfoDto userInfoDto) {
        userService.signupUser(userInfoDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }


}
