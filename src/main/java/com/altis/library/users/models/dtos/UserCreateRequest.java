package com.altis.library.users.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

@Schema(description = "Dados para cadastro do locatario")
public record UserCreateRequest(

        @Schema(description = "Nome completo do locatario", example = "João Guilherme")
        @NotBlank(message = "O nome é obrigatório!")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 caracteres!")
        String name,

        @Schema(description = "E-mail válido para login", example = "joao@gmail.com")
        @NotBlank(message = "O e-mail é obrigatório!")
        @Email(message = "O formato do e-mail é inválido!")
        String email,

        @Schema(description = "CPF válido do locatario", example = "111.222.333-44")
        @NotBlank(message = "O CPF é obrigatório!")
        @CPF(message = "CPF inválido!")
        String cpf,

        @Schema(description = "Senha de acesso", example = "Senha@123")
        @NotBlank(message = "A senha é obrigatória!")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres!")
        String password,

        @Schema(description = "Telefone", example = "85997377275")
        @NotBlank(message = "O telefone é obrigatório!")
        String phone,

        @Schema(description = "Endereço residencial completo", example = "Rua das Flores, 123 - Fortaleza/CE")
        @NotBlank(message = "O endereço é obrigatório!")
        String address,

        @Schema(description = "Data de nascimento no formato YYYY-MM-DD", example = "1998-05-20")
        @NotNull(message = "A data de nascimento é obrigatória!")
        @Past(message = "A data de nascimento deve ser uma data passada")
        LocalDate birthDate
) {}