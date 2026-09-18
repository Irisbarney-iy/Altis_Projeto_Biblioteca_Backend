package com.altis.library.books.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Informações para criação do livro")
public record BookCreateRequest(

        @Schema(description = "Titulo do livro", example = "As aventuras do Java")
        @NotBlank(message = "O titulo é obrigatório!")
        @Size(min = 2, max = 150, message = "O titulo tem que ter entre 2 e 150 caracteres")
        String title,

        @Schema(description = "Nome do autor do livro", example = "Gustavo Almeida")
        @NotBlank(message = "O autor é obrigatório!")
        @Size(min = 2, max = 100, message = "O autor tem que ter entre 2 e 100 caracteres")
        String author,

        @Schema(description = "Data de lançamento do livro", example = "2020")
        @NotNull(message = "A data de lançamento é obrigatória!")
        Integer releaseYear,

        @Schema(description = "Quantidade total de livros", example = "150")
        @NotNull(message = "A quantidade de livros é obrigatória!")
        @Positive(message = "A quantidade deve ser maior que zero!")
        Integer totalQuantity,

        @Schema(description = "Identificador da editora para relações", example = "2")
        @NotNull(message = "A editora é obrigatória!")
        Long publisherId

) {}
