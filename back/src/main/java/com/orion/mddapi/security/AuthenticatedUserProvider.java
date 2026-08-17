package com.orion.mddapi.security;

import com.orion.mddapi.entities.User;
import com.orion.mddapi.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserProvider {

    private final UserRepository userRepository;

    public AuthenticatedUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur authentifié introuvable"));
    }
}