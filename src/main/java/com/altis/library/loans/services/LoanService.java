package com.altis.library.loans.services;

import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.books.repositories.BookRepository;
import com.altis.library.loans.mappers.LoanMapper;
import com.altis.library.loans.models.dtos.LoanCreateRequest;
import com.altis.library.loans.models.dtos.LoanRenewRequest;
import com.altis.library.loans.models.dtos.LoanResponse;
import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.models.enums.LoansStatus;
import com.altis.library.loans.repositories.LoanRepository;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository, LoanMapper loanMapper) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loanMapper = loanMapper;
    }

    @Transactional
    public LoanResponse create(LoanCreateRequest request) {
        UserEntity user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalStateException("Usuário inativo não pode realizar empréstimos");
        }
        LocalDate today = LocalDate.now();

        boolean activeDelay = loanRepository.existsByUserIdAndStatus(user.getId(), LoansStatus.OVERDUE) || loanRepository.existsByUserIdAndStatusAndLimitTermBefore(user.getId(), LoansStatus.RENTED, today);

        if (activeDelay) {
            throw new IllegalStateException("Empréstimo negado! O usuário possui pendências em atraso.");
        }

        long activeLoans = loanRepository.countByUserIdAndStatusIn(
                user.getId(),
                List.of(LoansStatus.RENTED, LoansStatus.OVERDUE)
        );

        if (activeLoans >= 5) {
            throw new IllegalStateException("Empréstimo negado! O usuário atingiu o limite máximo de 5 empréstimos ativos");
        }

        BookEntity book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado!"));

        if (book.getInUseQuantity() >= book.getTotalQuantity()) {
            throw new IllegalStateException("Empréstimo negado! Livro sem estoque disponível");
        }

        book.setInUseQuantity(book.getInUseQuantity() + 1);
        bookRepository.save(book);

        LoanEntity loan = loanMapper.toEntity(request, user, book, today);
        LoanEntity savedLoan = loanRepository.save(loan);

        return loanMapper.toResponse(savedLoan);
    }

    @Transactional
    public LoanResponse returnBook(Long loanId) {
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado!"));

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Este empréstimo já foi finalizado e não pode ser alterado!");
        }

        LocalDate today = LocalDate.now();
        loan.setReturnedDate(today);

        if (today.isAfter(loan.getLimitTerm())) {
            loan.setStatus(LoansStatus.RETURNED_WITH_DELAY);
        } else {
            loan.setStatus(LoansStatus.RETURNED_ON_TIME);
        }

        BookEntity book = loan.getBook();
        book.setInUseQuantity(Math.max(0, book.getInUseQuantity() - 1));
        bookRepository.save(book);

        LoanEntity updatedLoan = loanRepository.save(loan);
        return loanMapper.toResponse(updatedLoan);
    }

    @Transactional(readOnly = true)
    public LoanResponse findById(Long id) {
        LoanEntity loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado"));
        return loanMapper.toResponse(updateStatusDelay(loan));
    }

    @Transactional(readOnly = true)
    public Page<LoanResponse> search(String term, Pageable pageable) {
        if (term == null || term.isBlank()) {
            return loanRepository.findAll(pageable)
                    .map(this::updateStatusDelay)
                    .map(loanMapper::toResponse);
        }
        return loanRepository.searchByTerm(term, pageable)
                .map(this::updateStatusDelay)
                .map(loanMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findByUserId(Long userId) {
        return loanRepository.findByUserId(userId)
                .stream()
                .map(this::updateStatusDelay)
                .map(loanMapper::toResponse)
                .toList();
    }

    private LoanEntity updateStatusDelay(LoanEntity loan) {
        if (loan.getStatus() == LoansStatus.RENTED && LocalDate.now().isAfter(loan.getLimitTerm())) {
            loan.setStatus(LoansStatus.OVERDUE);
        }
        return loan;
    }

    @Transactional
    public LoanResponse renew(Long id, LoanRenewRequest request) {
        LoanEntity loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado!"));

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Não é possível renovar um empréstimo que já foi devolvido!");
        }

        if (loan.getStatus() == LoansStatus.OVERDUE || LocalDate.now().isAfter(loan.getLimitTerm())) {
            throw new IllegalStateException("Não é possível renovar um empréstimo em atraso!");
        }

        loan.setLimitTerm(request.newTerm());

        LoanEntity updatedLoan = loanRepository.save(loan);
        return loanMapper.toResponse(updatedLoan);
    }
}