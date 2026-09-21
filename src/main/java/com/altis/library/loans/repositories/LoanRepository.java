package com.altis.library.loans.repositories;

import com.altis.library.loans.models.entities.LoanEntity;
import com.altis.library.loans.models.enums.LoansStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    long countByUserIdAndStatusIn(Long userId, List<LoansStatus> statuses);

    boolean existsByUserIdAndStatus(Long userId, LoansStatus status);

    boolean existsByUserIdAndStatusAndLimitTermBefore(Long userId, LoansStatus status, LocalDate limitTerm);

    @Query("SELECT l FROM LoanEntity l WHERE LOWER(l.user.name) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(l.book.title) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<LoanEntity> searchByTerm(@Param("term") String term, Pageable pageable);

    List<LoanEntity> findByUserId(Long userId);
}