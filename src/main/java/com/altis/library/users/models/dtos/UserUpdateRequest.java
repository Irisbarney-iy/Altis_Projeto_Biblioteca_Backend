package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Atualização dos dados do locatario")
public record UserUpdateRequest(

        @Schema(description = "Nome completo", example = "João Guilherme")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 caracteres!")
        String name,

        @Schema(description = "E-mail do locatario", example = "joao@gmail.com")
        @Email(message = "O formato do e-mail inválido!")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "O e-mail deve ser com @gmail.com!")
        @Size(max = 100, message = "O e-mail não pode ser mais de 100 caracteres!")
        String email,

        @Schema(description = "Numero do locatario", example = "85997377275")
        @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres!")
        String phone,

        @Schema(description = "Endereço do locatario", example = "Rua são pedro, 242 - São Paulo")
        @Size(max = 255, message = "O endereço não pode ter mais que 255 caracteres!")
        String address

) {}