package com.orion.mddapi.services;

import com.orion.mddapi.dto.UpdateProfileRequest;
import com.orion.mddapi.dto.UserProfileDto;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.UserRepository;
import com.orion.mddapi.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Le profil renvoie l'email, le username et les abonnements de l'utilisateur courant")
    void getCurrentUserProfile_returnsProfile() {
        // Arrange
        User alice = new User();
        alice.setId(1L);
        alice.setUsername("alice");
        alice.setEmail("alice@mail.com");

        Theme theme = new Theme();
        theme.setId(1L);
        theme.setTitle("JavaScript");

        Subscription subscription = new Subscription();
        subscription.setUser(alice);
        subscription.setTheme(theme);

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(alice);
        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of(subscription));

        // Act
        UserProfileDto result = userService.getCurrentUserProfile();

        // Assert
        assertThat(result.email()).isEqualTo("alice@mail.com");
        assertThat(result.username()).isEqualTo("alice");
        assertThat(result.subscriptions()).hasSize(1);
        assertThat(result.subscriptions().get(0).title()).isEqualTo("JavaScript");
    }

    @Test
    @DisplayName("Modifier le profil met à jour email/username et hache le mot de passe fourni")
    void updateProfile_withPassword_updatesAndHashes() {
        // Arrange
        User alice = new User();
        alice.setId(1L);
        alice.setEmail("old@mail.com");
        alice.setUsername("oldName");
        alice.setPassword("oldHash");

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(alice);
        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of());

        UpdateProfileRequest request = new UpdateProfileRequest("new@mail.com", "newName", "Password1!");

        // Act
        UserProfileDto result = userService.updateProfile(request);

        // Assert
        assertThat(result.email()).isEqualTo("new@mail.com");
        assertThat(result.username()).isEqualTo("newName");
        assertThat(alice.getPassword()).isNotEqualTo("Password1!");
        assertThat(alice.getPassword()).isNotEqualTo("oldHash");
    }

    @Test
    @DisplayName("Modifier le profil ne change pas le mot de passe s'il est vide")
    void updateProfile_blankPassword_keepsPassword() {
        // Arrange
        User alice = new User();
        alice.setId(1L);
        alice.setEmail("old@mail.com");
        alice.setUsername("oldName");
        alice.setPassword("oldHash");

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(alice);
        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of());

        UpdateProfileRequest request = new UpdateProfileRequest("new@mail.com", "newName", "");

        // Act
        userService.updateProfile(request);

        // Assert
        assertThat(alice.getPassword()).isEqualTo("oldHash");
    }
}
