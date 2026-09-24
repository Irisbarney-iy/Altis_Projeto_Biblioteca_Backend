package com.altis.library.loans.mappers;

import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.loans.models.dtos.LoanCreateRequest;
import com.altis.library.loans.models.dtos.LoanResponse;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.models.enums.LoansStatus;
import com.altis.library.users.models.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LoanMapper {

    public LoanEntity toEntity(LoanCreateRequest request, UserEntity user, BookEntity book, LocalDate loanDate) {
        if (request == null) return null;

        LoanEntity loan = new LoanEntity();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(loanDate);
        loan.setLimitTerm(request.termLimit());
        loan.setStatus(LoansStatus.RENTED);

        return loan;
    }

    public LoanResponse toResponse(LoanEntity entity) {
        if (entity == null) return null;

        Long userId = entity.getUser() != null ? entity.getUser().getId() : null;
        String userName = entity.getUser() != null ? entity.getUser().getName() : null;

        Long bookId = entity.getBook() != null ? entity.getBook().getId() : null;
        String bookTitle = entity.getBook() != null ? entity.getBook().getTitle() : null;

        return new LoanResponse(
                entity.getId(),
                userId,
                userName,
                bookId,
                bookTitle,
                entity.getLoanDate(),
                entity.getLimitTerm(),
                entity.getReturnedDate(),
                entity.getStatus()
        );
    }
}