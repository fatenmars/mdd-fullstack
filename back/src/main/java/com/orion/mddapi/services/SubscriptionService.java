package com.orion.mddapi.services;

import org.springframework.stereotype.Service;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.ThemeNotFoundException;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.ThemeRepository;
import com.orion.mddapi.security.AuthenticatedUserProvider;

@Service
public class SubscriptionService {

    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
            ThemeRepository themeRepository, AuthenticatedUserProvider authenticatedUserProvider) {
        this.subscriptionRepository = subscriptionRepository;
        this.themeRepository = themeRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public void subscribe(Long themeId) {
        User user = authenticatedUserProvider.getCurrentUser();

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("Thème introuvable (id: " + themeId + ")"));

        if (subscriptionRepository.existsByUserIdAndThemeId(user.getId(), themeId))
            return;

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(Long themeId) {
        Long userId = authenticatedUserProvider.getCurrentUser().getId();

        subscriptionRepository.findByUserIdAndThemeId(userId, themeId)
                .ifPresent(subscription -> subscriptionRepository.delete(subscription));
    }
}
