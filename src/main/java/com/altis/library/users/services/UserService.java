package com.altis.library.users.services;

import com.altis.library.users.models.dtos.UserCreateRequest;
import com.altis.library.users.models.dtos.UserResponse;
import com.altis.library.users.models.dtos.UserUpdateRequest;
import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request){
        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }
        if(userRepository.existsByCpf(request.cpf())){
            throw new IllegalArgumentException("CPF já cadastrado!");
        }

        UserEntity user = new UserEntity();
        user.setName(request.name());
        user.setEmail(request.email());
        String cpfNumberOnly = request.cpf().replaceAll("\\D", "");
        user.setCpf(cpfNumberOnly);
        String phoneNumberOnly = request.phone().replaceAll("\\D","");
        user.setPhone(phoneNumberOnly);
        user.setAddress(request.address());
        user.setBirthDate(request.birthDate());

        user.setPassword(passwordEncoder.encode(request.password()));

        user.setActive(true);
        user.setAdmin(false);

        UserEntity savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
        if(request.email() != null && !request.email().isBlank()) {
            if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("E-mail já está em uso!");
            }

            user.setEmail(request.email());
        }
        if(request.name() != null && !request.name().isBlank()){
            user.setName(request.name());
        }

        if(request.phone() != null && !request.phone().isBlank()) {
            user.setPhone(request.phone());
        }

        if(request.address() != null && !request.address().isBlank()) {
            user.setAddress(request.address());
        }

        UserEntity updatedUser = userRepository.save(user);
        return toResponse(updatedUser);

    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> search(String term, Pageable pageable){
        return userRepository.findByNameContainingIgnoreCaseOrCpfContainingOrEmailContainingIgnoreCase(
                term, term, term, pageable).map(this::toResponse);
    }

    @Transactional
    public void inactive(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activate(Long id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
        user.setActive(true);
        userRepository.save(user);
    }

    private UserResponse toResponse(UserEntity entity){
        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getAddress(),
                entity.getActive(),
                entity.getAdmin()
        );
    }
}
