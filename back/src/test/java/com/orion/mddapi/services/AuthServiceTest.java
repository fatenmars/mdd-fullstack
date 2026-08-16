package com.orion.mddapi.services;

import com.orion.mddapi.dto.RegisterRequest;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.UserAlreadyExistsException;
import com.orion.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("L'inscription crée un utilisateur quand email et username sont libres")
    void register_createsUser() {
        when(userRepository.existsByEmail("zoe@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("zoe")).thenReturn(false);

        RegisterRequest request = new RegisterRequest("zoe@mail.com", "zoe", "Password1!");

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("L'inscription échoue si l'email est déjà pris")
    void register_emailTaken_throws() {
        when(userRepository.existsByEmail("zoe@mail.com")).thenReturn(true);

        RegisterRequest request = new RegisterRequest("zoe@mail.com", "zoe", "Password1!");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("L'inscription échoue si le nom d'utilisateur est déjà pris")
    void register_usernameTaken_throws() {
        when(userRepository.existsByEmail("zoe@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("zoe")).thenReturn(true);

        RegisterRequest request = new RegisterRequest("zoe@mail.com", "zoe", "Password1!");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(UserAlreadyExistsException.class);
        verify(userRepository, never()).save(any(User.class));
    }
}