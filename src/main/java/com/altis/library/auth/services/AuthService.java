package com.altis.library.auth.services;

import com.altis.library.auth.models.dtos.*;
import com.altis.library.security.TokenService;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public TokenResponse login(LoginRequest data) {
        var user = userRepository.findByEmailIgnoreCase(data.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));

        if(!passwordEncoder.matches(data.password(), user.getPassword())) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }
        var token = tokenService.generateToken(user);

        String role = Boolean.TRUE.equals(user.getAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
        return new TokenResponse(token, role);
    }

    @Transactional(readOnly = true)
    public RecoveryTokenResponse validatePasswordReset(PasswordResetValidateRequest request) {
        UserEntity user = userRepository.findByEmailIgnoreCaseAndCpf(request.email().trim(), request.cpf().trim())
                .orElseThrow(() -> new IllegalArgumentException("Dados de identificação inválidos, e-mail ou CPF não conferem."));

        String recoveryToken = tokenService.generateRecoveryToken(user.getEmail());
        return new RecoveryTokenResponse(recoveryToken);
    }

    @Transactional
    public void resetPassword(PasswordResetChangeRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("A nova senha e a confirmação de senha não coincidem.");
        }

        String email = tokenService.validateRecoveryToken(request.recoveryToken());

        UserEntity user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}