package com.orion.mddapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.orion.mddapi.dto.ThemeListItemDto;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.ThemeRepository;
import com.orion.mddapi.security.AuthenticatedUserProvider;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("La liste des thèmes renvoie tous les thèmes")
    void getAllThemes_returnsAllThemes() {
        // Arrange
        Theme theme1 = new Theme();
        theme1.setId(1L);
        theme1.setTitle("JavaScript");
        theme1.setDescription("desc JS");

        Theme theme2 = new Theme();
        theme2.setId(2L);
        theme2.setTitle("Java");
        theme2.setDescription("desc Java");

        User alice = new User();
        alice.setId(1L);

        Subscription subscription = new Subscription();
        subscription.setUser(alice);
        subscription.setTheme(theme1);

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(alice);
        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of(subscription));
        when(themeRepository.findAll()).thenReturn(List.of(theme1, theme2));

        // Act
        List<ThemeListItemDto> result = themeService.getAllThemes();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).title()).isEqualTo("JavaScript");
        assertThat(result.get(0).subscribed()).isTrue();
        assertThat(result.get(1).subscribed()).isFalse();
    }
}
