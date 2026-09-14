package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Informações do usuario devolvido pela api")
public record UserResponse(

        @Schema(description = "Identificador do usuário", example = "1")
        Long id,

        @Schema(description = "Nome do usuário", example = "João Guilherme")
        String name,

        @Schema(description = "E-mail do usuário", example = "joao@gmail.com")
        String email,

        @Schema(description = "CPF do usuário", example = "111.222.333-44")
        String cpf,

        @Schema(description = "Telefone de contato", example = "85997377275")
        String phone,

        @Schema(description = "Data de nascimento do usuário", example = "1998-05-20")
        LocalDate birthDate,

        @Schema(description = "Endereço completo", example = "Rua das Flores, 123 - Fortaleza/CE")
        String address,

        @Schema(description = "Usuário ativo ou inativo", example = "true")
        Boolean active,

        @Schema(description = "Usuário administrador ou não", example = "false")
        Boolean isAdmin
) {}