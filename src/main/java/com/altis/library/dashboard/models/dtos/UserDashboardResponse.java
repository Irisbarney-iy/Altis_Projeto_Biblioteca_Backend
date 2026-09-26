package com.altis.library.dashboard.models.dtos;

import java.time.LocalDate;
import java.util.List;

public record UserDashboardResponse(
        long activeLoans,
        long totalDelays,
        long totalReturned,
        List<MyBookDTO> myBooks
) {
    public record MyBookDTO(
            String bookTitle,
            String author,
            LocalDate loanDate,
            LocalDate limitTerm,
            long daysOverdue
    ) {}
}