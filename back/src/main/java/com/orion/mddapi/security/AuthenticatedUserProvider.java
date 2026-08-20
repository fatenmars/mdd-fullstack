package com.orion.mddapi.security;

import com.orion.mddapi.entities.User;
import com.orion.mddapi.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Donne accès à l'utilisateur authentifié pour la requête courante, à partir du
 * nom d'utilisateur placé dans le contexte de sécurité par {@link JwtAuthenticationFilter}.
 * Centralise cette récupération pour éviter de la dupliquer dans chaque service.
 */
@Component
public class AuthenticatedUserProvider {

    private final UserRepository userRepository;

    public AuthenticatedUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** Récupère en base l'utilisateur associé au token de la requête courante. */
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur authentifié introuvable"));
    }
}