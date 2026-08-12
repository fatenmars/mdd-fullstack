package com.orion.mddapi.services;

import com.orion.mddapi.repositories.ArticleRepository;
import com.orion.mddapi.repositories.SubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.entities.Theme;
import com.orion.mddapi.entities.Subscription;
import com.orion.mddapi.dto.ArticleDto;
import com.orion.mddapi.entities.Article;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("Le fil renvoie les articles des thèmes suivis")
    void getFeed_returnsArticlesForSubscribedThemes() {

        // Arrange
        User alice = new User();
        alice.setId(1L);
        alice.setUsername("alice");

        Theme theme = new Theme();
        theme.setId(1L);
        theme.setTitle("Technology");

        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setUser(alice);
        subscription.setTheme(theme);

        Article article = new Article();
        article.setId(1L);
        article.setTitle("Latest Tech News");
        article.setAuthor(alice);
        article.setTheme(theme);
        article.setContent("Some content about technology.");
        article.setCreatedAt(LocalDateTime.now());

        when(subscriptionRepository.findAllByUserId(1L)).thenReturn(List.of(subscription));
        when(articleRepository.findAllByThemeIn(anyList(), any(Sort.class))).thenReturn(List.of(article));

        // Act
        List<ArticleDto> feed = articleService.getFeed("desc");

        // Assert
        assertThat(feed).hasSize(1);
        assertThat(feed.get(0).title()).isEqualTo(article.getTitle());
    }
}