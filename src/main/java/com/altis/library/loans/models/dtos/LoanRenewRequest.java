package com.altis.library.loans.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Dados para renovação do prazo de empréstimo")
public record LoanRenewRequest(

        @Schema(description = "Nova data de prazo limite", example = "2026-10-15")
        @NotNull(message = "A nova data de prazo é obrigatória!")
        @FutureOrPresent(message = "A nova data limite não pode ser uma data passada")
        LocalDate newTerm
) {}