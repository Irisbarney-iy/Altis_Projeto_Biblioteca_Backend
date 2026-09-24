package com.altis.library.auth.dtos;

public record TokenResponse(
        String token,
        String type,
        String role
) {
    public TokenResponse(String token, String role) {
        this(token, "Bearer", role);
    }
}