package com.altis.library.books.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações do livro retornadas pela api")
public record BookResponse(

        @Schema(description = "Identificador do livro", example = "1")
        Long id,

        @Schema(description = "Titulo do livro", example = "As aventuras do Java")
        String title,

        @Schema(description = "Nome do autor do livro", example = "Gustavo Almeida")
        String author,

        @Schema(description = "Data de lançamento do livro", example = "2020")
        Integer releaseYear,

        @Schema(description = "Quantidade total de livros", example = "150")
        Integer totalQuantity,

        @Schema(description = "Quantidade de livros em uso", example = "60")
        Integer inUseQuantity,

        @Schema(description = "Identificador da editora para relações", example = "2")
        Long publisherId,

        @Schema(description = "Nome da editora", example = "Record News")
        String publisherName

) {}