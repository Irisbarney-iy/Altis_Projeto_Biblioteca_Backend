package com.altis.library.books.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Informações para a atualização do livro")
public record BookUpdateRequest(

        @Schema(description = "Titulo do livro", example = "As aventuras do Java")
        @Size(min = 2, max = 150, message = "O titulo tem que ter entre 2 e 150 caracteres")
        String title,

        @Schema(description = "Nome do autor do livro", example = "Gustavo Almeida")
        @Size(min = 2, max = 100, message = "O autor tem que ter entre 2 e 100 caracteres")
        String author,

        @Schema(description = "Data de lançamento do livro", example = "2020")
        @Min(value = 1500, message = "O ano de lançamento deve ser maior que 1500")
        Integer releaseYear,

        @Schema(description = "Quantidade total de livros", example = "150")
        @PositiveOrZero(message = "A quantidade deve ser maior igual ou maior qur zero!")
        Integer totalQuantity,

        @Schema(description = "Identificador da editora para relações", example = "2")
        Long publisherId

) {}