package com.orion.mddapi.services;

import com.orion.mddapi.dto.UserProfileDto;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

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

        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of(subscription));

        // Act
        UserProfileDto result = userService.getCurrentUserProfile();

        // Assert
        assertThat(result.email()).isEqualTo("alice@mail.com");
        assertThat(result.username()).isEqualTo("alice");
        assertThat(result.subscriptions()).hasSize(1);
        assertThat(result.subscriptions().get(0).title()).isEqualTo("JavaScript");
    }
}