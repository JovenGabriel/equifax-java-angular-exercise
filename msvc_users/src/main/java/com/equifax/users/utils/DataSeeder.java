package com.equifax.users.utils;

import com.equifax.users.entities.User;
import com.equifax.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {
        if (!userRepository.existsByEmail("gabriel@gabriel.cl")) {
            User admin = User.builder()
                    .name("Admin")
                    .password(passwordEncoder.encode("Admin123"))
                    .rut("17421500-6")
                    .email("gabriel@gabriel.cl")
                    .build();
            userRepository.save(admin);
        }
    }

}
