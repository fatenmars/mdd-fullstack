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
import org.springframework.data.domain.Sort;
import com.orion.mddapi.dto.ArticleDetailDto;
import com.orion.mddapi.dto.CommentDto;
import com.orion.mddapi.exceptions.ArticleNotFoundException;

@Service
public class ArticleService {

        private final ArticleRepository articleRepository;
        private final SubscriptionRepository subscriptionRepository;
        private final CommentService commentService;

        public ArticleService(ArticleRepository articleRepository, SubscriptionRepository subscriptionRepository,
                        CommentService commentService) {
                this.articleRepository = articleRepository;
                this.subscriptionRepository = subscriptionRepository;
                this.commentService = commentService;
        }

        public List<ArticleDto> getFeed(String order) {
                Long userId = 1L;
                Sort sort = "asc".equals(order) ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
                List<Subscription> subscriptions = subscriptionRepository.findAllByUserId(userId);
                List<Theme> themes = subscriptions.stream()
                                .map(Subscription::getTheme)
                                .toList();
                return articleRepository.findAllByThemeIn(themes, sort)
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

        public ArticleDetailDto getArticleDetail(Long articleId) {
                Article article = articleRepository.findById(articleId)
                                .orElseThrow(() -> new ArticleNotFoundException(
                                                "Article introuvable (id: " + articleId + ")"));
                AuthorDto authorDto = new AuthorDto(article.getAuthor().getId(), article.getAuthor().getUsername());
                ThemeDto themeDto = new ThemeDto(article.getTheme().getId(), article.getTheme().getTitle());
                List<CommentDto> comments = commentService.getCommentsByArticleId(articleId);
                return new ArticleDetailDto(article.getId(), article.getTitle(), article.getContent(),
                                article.getCreatedAt(),
                                authorDto, themeDto, comments);
        }
}