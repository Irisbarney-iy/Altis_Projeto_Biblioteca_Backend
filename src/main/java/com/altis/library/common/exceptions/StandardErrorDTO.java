package com.altis.library.common.exceptions;

import java.time.Instant;
import java.util.List;

public record StandardErrorDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldErrorDTO> errors
) {
    public StandardErrorDTO(Integer status, String error, String message, String path) {
        this(Instant.now(), status, error, message, path, null);
    }

    public record FieldErrorDTO(String field, String message) {}
}