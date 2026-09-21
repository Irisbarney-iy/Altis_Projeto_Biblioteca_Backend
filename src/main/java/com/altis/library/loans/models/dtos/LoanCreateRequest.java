package com.altis.library.loans.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Informações para empréstimos")
public record LoanCreateRequest(

        @Schema(description = "Identificador do usuário", example = "1")
        @NotNull(message = "O ID do usuário é obrigatório!")
        Long userId,

        @Schema(description = "Identificador do livro", example = "2")
        @NotNull(message = "O ID do livro é obrigatório!")
        Long bookId,

        @Schema(description = "Data limite de devolução", example = "2026-10-10")
        @NotNull(message = "A data de prazo limite é obrigatória!")
        @FutureOrPresent(message = "A data limite não pode ser uma data de antes ou agora")
        LocalDate termLimit
) {}