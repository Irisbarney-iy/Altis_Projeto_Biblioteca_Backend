package com.altis.library.config;

import com.altis.library.users.models.entities.UserEntity;
import com.altis.library.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setName("Administrador do Sistema");
            admin.setEmail("admin@admin.com");
            admin.setCpf("00000000000");
            admin.setBirthDate(LocalDate.of(2000, 1, 1));
            admin.setPassword(passwordEncoder.encode("12345678"));
            admin.setPhone("00000000000");
            admin.setAddress("Admin");
            admin.setActive(true);
            admin.setAdmin(true);

            userRepository.save(admin);
        }
    }
}