package com.altis.library.auth.dtos;

public record TokenResponse(
        String token,
        String type
) {
    public TokenResponse(String token) {
        this(token, "Bearer");
    }
}