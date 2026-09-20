package com.altis.library.publishers.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record PublisherUpdateRequest(

        @Schema(description = "Nome da editora", example = "Record News")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 caracteres!")
        String name,

        @Schema(description = "E-mail da editora", example = "recordnews@gmail.com")
        @Email(message = "Formato de e-mail inválido!")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "O e-mail deve obrigatoriamente ser do domínio @gmail.com!")
        @Size(max = 100, message = "O e-mail não pode ter mais que 100 caracteres!")
        String email,

        @Schema(description = "Telefone da editora", example = "85997387176")
        @Size(min = 8, max = 20, message = "O telefone deve ter entre 8 e 20 caracteres!")
        String phone,

        @Schema(description = "Site da editora", example = "www.record.com")
        @Size(max = 100, message = "O site não pode ter mais que 100 caracteres!")
        String site

) {}