package com.altis.library.auth.services;

import com.altis.library.auth.dtos.LoginRequest;
import com.altis.library.auth.dtos.TokenResponse;
import com.altis.library.security.TokenService;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        var user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));

        if(!passwordEncoder.matches(data.password(), user.getPassword())) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }
        var token = tokenService.generateToken(user);
        return new TokenResponse(token);
    }
}