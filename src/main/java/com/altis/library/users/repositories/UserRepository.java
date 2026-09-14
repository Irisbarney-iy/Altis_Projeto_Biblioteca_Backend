package com.altis.library.users.repositories;

import com.altis.library.users.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    Page<UserEntity>findByNameContainingIgnoreCaseOrCpfContainingOrEmailContainingIgnoreCase(String name, String cpf, String email, Pageable pageable);
}