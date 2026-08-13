package com.orion.mddapi.services;

import com.orion.mddapi.dto.ThemeListItemDto;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.repositories.ThemeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ThemeService(ThemeRepository themeRepository, SubscriptionRepository subscriptionRepository) {
        this.themeRepository = themeRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<ThemeListItemDto> getAllThemes() {
        Long userId = 1L;

        Set<Long> subscribedThemeIds = subscriptionRepository.findAllByUserId(userId).stream()
                .map(subscription -> subscription.getTheme().getId()).collect(Collectors.toSet());
        return themeRepository.findAll().stream().map(theme -> new ThemeListItemDto(theme.getId(), theme.getTitle(),
                theme.getDescription(), subscribedThemeIds.contains(theme.getId()))).toList();
    }
}