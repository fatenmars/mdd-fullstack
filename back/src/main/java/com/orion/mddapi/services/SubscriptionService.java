package com.orion.mddapi.services;

import org.springframework.stereotype.Service;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.ThemeNotFoundException;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.ThemeRepository;
import com.orion.mddapi.repositories.UserRepository;

@Service
public class SubscriptionService {

    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository,
            ThemeRepository themeRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
    }

    public void subscribe(Long themeId) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Utilisateur courant introuvable"));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new ThemeNotFoundException("Thème introuvable (id: " + themeId + ")"));

        if (subscriptionRepository.existsByUserIdAndThemeId(user.getId(), themeId))
            return;

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        subscriptionRepository.save(subscription);
    }

}
