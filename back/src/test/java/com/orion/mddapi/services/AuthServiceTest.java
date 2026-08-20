package com.orion.mddapi.services;

import com.orion.mddapi.dto.LoginRequest;
import com.orion.mddapi.dto.RegisterRequest;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.InvalidCredentialsException;
import com.orion.mddapi.exceptions.UserAlreadyExistsException;
import com.orion.mddapi.repositories.UserRepository;
import com.orion.mddapi.security.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

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

    @Test
    @DisplayName("La connexion renvoie un token quand les identifiants sont valides")
    void login_returnsToken() {
        User bob = new User();
        bob.setUsername("bob");
        bob.setPassword(new BCryptPasswordEncoder().encode("Password1!")); // hash réel

        when(userRepository.findByEmailOrUsername("bob", "bob")).thenReturn(Optional.of(bob));
        when(jwtService.generateToken("bob")).thenReturn("fake-jwt-token");

        LoginRequest request = new LoginRequest("bob", "Password1!");

        String token = authService.login(request);

        assertThat(token).isEqualTo("fake-jwt-token");
    }

    @Test
    @DisplayName("La connexion échoue si l'utilisateur est introuvable")
    void login_userNotFound_throws() {
        when(userRepository.findByEmailOrUsername("nobody", "nobody")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("nobody", "whatever");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("La connexion échoue si le mot de passe est incorrect")
    void login_wrongPassword_throws() {
        User bob = new User();
        bob.setUsername("bob");
        bob.setPassword(new BCryptPasswordEncoder().encode("Password1!"));

        when(userRepository.findByEmailOrUsername("bob", "bob")).thenReturn(Optional.of(bob));

        LoginRequest request = new LoginRequest("bob", "WrongPassword1!");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}