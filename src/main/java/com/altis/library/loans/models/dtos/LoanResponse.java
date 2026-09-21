package com.altis.library.loans.models.dtos;

import com.altis.library.loans.models.enums.LoansStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Informações do empréstimo retornadas pela API")
public record LoanResponse(

        @Schema(description = "Identificador do empréstimo", example = "10")
        Long id,

        @Schema(description = "Identificador do usuário", example = "1")
        Long userId,

        @Schema(description = "Nome do usuário", example = "João Guilherme")
        String userName,

        @Schema(description = "Identificador do livro", example = "2")
        Long bookId,

        @Schema(description = "Título do livro", example = "As aventuras do Java")
        String bookTitle,

        @Schema(description = "Data em que o empréstimo foi realizado", example = "2026-09-20")
        LocalDate loanDate,

        @Schema(description = "Data limite para devolução", example = "2026-10-04")
        LocalDate limitTerm,

        @Schema(description = "Data efetiva da devolução", example = "2026-09-28")
        LocalDate returnDate,

        @Schema(description = "Status atual do empréstimo", example = "ALUGADO")
        LoansStatus status
) {}