package com.orion.mddapi.repositories;

import com.orion.mddapi.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    // User getUserByEmailOrByUsername(String identifier);
    Optional<User> findByEmailOrUsername(String email, String username);
}
