package com.equifax.users.repositories;

import com.equifax.users.entities.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@NotNull String email);
    boolean existsByRut(@NotNull String rut);
    Optional<User> findByEmail(String email);
}
