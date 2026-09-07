package com.altis.library.loans.models.entities;

import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.loans.models.enums.LoansStatus;
import com.altis.library.users.models.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false)
    private LocalDate prazoLimite;

    @Column
    private LocalDate dataDevolucao;

    @Enumerated(EnumType.STRING)
    private LoansStatus status;

}
