package com.altis.library.dashboard.services;

import com.altis.library.books.repositories.BookRepository;
import com.altis.library.dashboard.models.dtos.AdminDashboardResponse;
import com.altis.library.dashboard.models.dtos.UserDashboardResponse;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.models.enums.LoansStatus;
import com.altis.library.loans.repositories.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DashboardService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public DashboardService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public AdminDashboardResponse getAdminDashboard() {
        LocalDate today = LocalDate.now();

        long totalBooks = bookRepository.findAll()
                .stream()
                .mapToLong(book -> book.getTotalQuantity() != null ? book.getTotalQuantity() : 0)
                .sum();

        long activeLoans = loanRepository.countByStatusIn(List.of(LoansStatus.RENTED, LoansStatus.OVERDUE));

        LocalDate startOfMonth = YearMonth.now().atDay(1);
        LocalDate endOfMonth = YearMonth.now().atEndOfMonth();
        long monthlyLoans = loanRepository.countByLoanDateBetween(startOfMonth, endOfMonth);

        List<LoanEntity> overdueLoans = loanRepository.findByStatusOrLimitTermBeforeAndReturnedDateIsNull(
                LoansStatus.OVERDUE, today
        );

        List<AdminDashboardResponse.CriticalDelayDTO> criticalDelays = overdueLoans.stream()
                .filter(loan -> today.isAfter(loan.getLimitTerm()))
                .sorted((l1, l2) -> l1.getLimitTerm().compareTo(l2.getLimitTerm()))
                .limit(5)
                .map(loan -> new AdminDashboardResponse.CriticalDelayDTO(
                        loan.getUser().getName(),
                        loan.getBook().getTitle(),
                        ChronoUnit.DAYS.between(loan.getLimitTerm(), today)
                ))
                .toList();

        return new AdminDashboardResponse(totalBooks, activeLoans, monthlyLoans, criticalDelays);
    }

    @Transactional(readOnly = true)
    public UserDashboardResponse getUserDashboard(Long userId) {
        LocalDate today = LocalDate.now();

        long activeLoans = loanRepository.countByUserIdAndStatusIn(
                userId, List.of(LoansStatus.RENTED, LoansStatus.OVERDUE)
        );

        List<LoanEntity> userLoans = loanRepository.findByUserId(userId);
        long totalDelays = userLoans.stream()
                .filter(loan -> loan.getReturnedDate() == null && today.isAfter(loan.getLimitTerm()))
                .count();

        long totalReturned = userLoans.stream()
                .filter(loan -> loan.getReturnedDate() != null)
                .count();

        List<UserDashboardResponse.MyBookDTO> myBooks = userLoans.stream()
                .filter(loan -> loan.getReturnedDate() == null)
                .limit(5)
                .map(loan -> {
                    long daysOverdue = today.isAfter(loan.getLimitTerm())
                            ? ChronoUnit.DAYS.between(loan.getLimitTerm(), today)
                            : 0;

                    return new UserDashboardResponse.MyBookDTO(
                            loan.getBook().getTitle(),
                            loan.getBook().getAuthor(),
                            loan.getLoanDate(),
                            loan.getLimitTerm(),
                            daysOverdue
                    );
                })
                .toList();

        return new UserDashboardResponse(activeLoans, totalDelays, totalReturned, myBooks);
    }
}