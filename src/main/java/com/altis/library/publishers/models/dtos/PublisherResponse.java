package com.altis.library.publishers.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Informações da editora retornadas pela api")
public record PublisherResponse(

        @Schema(description = "Identificador da editora", example = "1")
        Long id,

        @Schema(description = "Nome da editora", example = "Record News")
        String name,

        @Schema(description = "E-mail da editora", example = "recordnews@gmail.com")
        String email,

        @Schema(description = "Telefone da editora", example = "85997387176")
        String phone,

        @Schema(description = "Site da editora", example = "www.record.com")
        String site,

        @Schema(description = "Data de criação da editora", example = "2026/09/12")
        LocalDateTime createdDate,

        @Schema(description = "Data de atualização da editora", example = "2026/09/12")
        LocalDateTime updatedDate

) {}