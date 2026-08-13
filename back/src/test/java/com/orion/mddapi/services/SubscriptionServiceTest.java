package com.orion.mddapi.services;

import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.ThemeNotFoundException;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.ThemeRepository;
import com.orion.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    @DisplayName("S'abonner crée un abonnement quand il n'existe pas encore")
    void subscribe_createsSubscription() {
        User alice = new User();
        alice.setId(1L);
        Theme theme = new Theme();
        theme.setId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(themeRepository.findById(3L)).thenReturn(Optional.of(theme));
        when(subscriptionRepository.existsByUserIdAndThemeId(1L, 3L)).thenReturn(false);

        subscriptionService.subscribe(3L);

        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    @DisplayName("S'abonner ne crée pas de doublon si déjà abonné")
    void subscribe_alreadySubscribed_doesNotSave() {
        User alice = new User();
        alice.setId(1L);
        Theme theme = new Theme();
        theme.setId(3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(themeRepository.findById(3L)).thenReturn(Optional.of(theme));
        when(subscriptionRepository.existsByUserIdAndThemeId(1L, 3L)).thenReturn(true);

        subscriptionService.subscribe(3L);

        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    @DisplayName("S'abonner à un thème inexistant lève ThemeNotFoundException")
    void subscribe_themeNotFound_throws() {
        User alice = new User();
        alice.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(themeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscriptionService.subscribe(999L))
                .isInstanceOf(ThemeNotFoundException.class);
    }
}