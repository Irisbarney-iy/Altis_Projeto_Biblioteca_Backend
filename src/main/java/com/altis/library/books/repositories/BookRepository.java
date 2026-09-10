package com.altis.library.books.repositories;

import com.altis.library.books.models.entities.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    Page<BookEntity> findByActiveTrue(Pageable pageable);
}