package com.altis.library.loans.repositories;

import com.altis.library.loans.models.entities.LoanEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findByUserId(Long userId);
    Page<LoanEntity> findByActiveTrue(Pageable pageable);
}