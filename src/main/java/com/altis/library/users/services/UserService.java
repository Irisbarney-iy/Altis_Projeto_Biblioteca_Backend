package com.altis.library.users.services;

import com.altis.library.users.mappers.UserMapper;
import com.altis.library.users.models.dtos.UserCreateRequest;
import com.altis.library.users.models.dtos.UserResponse;
import com.altis.library.users.models.dtos.UserUpdateRequest;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }
        if (userRepository.existsByCpf(request.cpf())) {
            throw new IllegalArgumentException("cpf já cadastrado!");
        }

        UserEntity user = userMapper.toEntity(request);
        UserEntity savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));

        if (request.email() != null && !request.email().isBlank()) {
            if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("E-mail já está em uso!");
            }
        }

        userMapper.updateEntityFromDto(request, user);
        UserEntity updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> search(String term, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCaseOrCpfContainingOrEmailContainingIgnoreCase(
                term, term, term, pageable).map(userMapper::toResponse);
    }

    @Transactional
    public void inactive(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activate(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
        user.setActive(true);
        userRepository.save(user);
    }
}