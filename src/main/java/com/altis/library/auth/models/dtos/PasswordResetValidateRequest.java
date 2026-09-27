package com.altis.library.auth.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordResetValidateRequest(
        @NotBlank(message = "O e-mail é obrigatório")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "O e-mail deve ser do tipo gmail.com")
        String email,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf
) {}