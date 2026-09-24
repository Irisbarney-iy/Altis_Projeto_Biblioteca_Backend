package com.altis.library.users.mappers;

import com.altis.library.users.models.dtos.UserCreateRequest;
import com.altis.library.users.models.dtos.UserResponse;
import com.altis.library.users.models.dtos.UserUpdateRequest;
import com.altis.library.users.models.entities.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity toEntity(UserCreateRequest request) {
        if (request == null) return null;

        UserEntity user = new UserEntity();
        user.setName(request.name());
        user.setEmail(request.email());

        if (request.cpf() != null) {
            user.setCpf(request.cpf().replaceAll("\\D", ""));
        }
        if (request.phone() != null) {
            user.setPhone(request.phone().replaceAll("\\D", ""));
        }

        user.setAddress(request.address());
        user.setBirthDate(request.birthDate());
        user.setPassword(passwordEncoder.encode(request.password()));

        user.setActive(true);
        user.setAdmin(false);

        return user;
    }

    public void updateEntityFromDto(UserUpdateRequest request, UserEntity user) {
        if (request == null || user == null) return;

        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email());
        }
        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }
        if (request.phone() != null && !request.phone().isBlank()) {
            user.setPhone(request.phone());
        }
        if (request.address() != null && !request.address().isBlank()) {
            user.setAddress(request.address());
        }
    }

    public UserResponse toResponse(UserEntity entity) {
        if (entity == null) return null;

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