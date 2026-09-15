package com.altis.library.publishers.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record PublisherUpdateRequest(

        @Schema(description = "Nome da editora", example = "Record News")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 a 100 caracteres!")
        String name,

        @Schema(description = "E-mail da editora", example = "recordnews@gmail.com")
        String email,

        @Schema(description = "Telefone da editora", example = "85997387176")
        String phone,

        @Schema(description = "Site da editora", example = "www.record.com")
        String site

) {}