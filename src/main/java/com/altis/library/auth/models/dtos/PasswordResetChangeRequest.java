package com.altis.library.auth.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetChangeRequest(
        @NotBlank(message = "O token de recuperação é obrigatório")
        String recoveryToken,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 8, max = 15, message = "A senha deve ter entre 8 e 15 caracteres")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$", message = "A senha deve conter letras, números e caracteres especiais")
        @Schema(example = "Senha123@")
        String newPassword,

        @NotBlank(message = "A confirmação de senha é obrigatória")
        @Schema(example = "Senha123@")
        String confirmPassword
) {}