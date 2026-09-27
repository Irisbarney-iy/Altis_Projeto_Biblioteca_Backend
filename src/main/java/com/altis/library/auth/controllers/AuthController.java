package com.altis.library.auth.controllers;

import com.altis.library.auth.models.dtos.*;
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

    @PostMapping("/recover-password/validate")
    public ResponseEntity<RecoveryTokenResponse> validateRecovery(@RequestBody @Valid PasswordResetValidateRequest request) {
        RecoveryTokenResponse response = authService.validatePasswordReset(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recover-password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetChangeRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }
}