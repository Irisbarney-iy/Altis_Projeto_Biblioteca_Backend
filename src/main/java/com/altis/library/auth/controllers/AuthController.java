package com.altis.library.auth.controllers;

import com.altis.library.auth.dtos.LoginRequest;
import com.altis.library.auth.dtos.TokenResponse;
import com.altis.library.auth.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest data) {
        var response = authService.login(data);
        return ResponseEntity.ok(response);
    }
}