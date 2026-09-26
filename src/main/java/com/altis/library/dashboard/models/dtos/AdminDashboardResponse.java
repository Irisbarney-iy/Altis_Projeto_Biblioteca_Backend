package com.altis.library.dashboard.models.dtos;

import java.util.List;

public record AdminDashboardResponse(
        long totalBooks,
        long activeLoans,
        long monthlyLoans,
        List<CriticalDelayDTO> criticalDelays
) {
    public record CriticalDelayDTO(
            String userName,
            String bookTitle,
            long daysOverdue
    ) {}
}