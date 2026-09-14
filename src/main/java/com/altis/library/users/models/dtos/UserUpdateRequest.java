package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Atualização dos dados do locatario")
public record UserUpdateRequest(

        @Schema(description = "Nome completo", example = "João Guilherme")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 caracteres!")
        String name,

        @Schema(description = "E-mail do locatario", example = "joao@gmail.com")
        @Email(message = "O formato do e-mail inválido!")
        String email,

        @Schema(description = "Numero do locatario", example = "85997377275")
        String phone,

        @Schema(description = "Endereço do locatario", example = "Rua são pedro, 242 - São Paulo")
        String address

) {}