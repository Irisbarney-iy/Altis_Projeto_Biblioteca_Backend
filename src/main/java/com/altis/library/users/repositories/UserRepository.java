package com.altis.library.users.repositories;

import com.altis.library.users.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByEmailIgnoreCaseAndCpf(String email, String cpf);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByCpf(String cpf);

    @Query("SELECT u FROM UserEntity u WHERE " +
            ":term IS NULL OR :term = '' OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "u.cpf LIKE CONCAT('%', :term, '%')")
    Page<UserEntity> searchByTerm(@Param("term") String term, Pageable pageable);
}