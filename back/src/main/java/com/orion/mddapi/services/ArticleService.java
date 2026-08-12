package com.orion.mddapi.services;

import org.springframework.stereotype.Service;
import com.orion.mddapi.repositories.ArticleRepository;
import com.orion.mddapi.entities.Article;
import java.util.List;
import com.orion.mddapi.dto.ArticleDto;
import com.orion.mddapi.dto.AuthorDto;
import com.orion.mddapi.dto.ThemeDto;
import com.orion.mddapi.repositories.SubscriptionRepository;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.entities.Theme;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final SubscriptionRepository subscriptionRepository;

    public ArticleService(ArticleRepository articleRepository, SubscriptionRepository subscriptionRepository) {
        this.articleRepository = articleRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<ArticleDto> getFeed() {
        Long userId = 1L;
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(userId);
        List<Theme> themes = subscriptions.stream()
                .map(Subscription::getTheme)
                .toList();
        return articleRepository.findAllByThemeInOrderByCreatedAtDesc(themes)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public ArticleDto convertToDto(Article article) {
        AuthorDto authorDto = new AuthorDto(article.getAuthor().getId(), article.getAuthor().getUsername());
        ThemeDto themeDto = new ThemeDto(article.getTheme().getId(), article.getTheme().getTitle());
        return new ArticleDto(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(),
                authorDto, themeDto);
    }
}
