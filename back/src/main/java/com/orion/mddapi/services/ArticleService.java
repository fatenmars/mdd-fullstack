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
import com.orion.mddapi.entities.User;
import org.springframework.data.domain.Sort;
import com.orion.mddapi.dto.ArticleDetailDto;
import com.orion.mddapi.dto.CommentDto;
import com.orion.mddapi.dto.CreateArticleRequest;
import com.orion.mddapi.exceptions.ArticleNotFoundException;
import com.orion.mddapi.exceptions.ThemeNotFoundException;
import com.orion.mddapi.repositories.ThemeRepository;
import com.orion.mddapi.repositories.UserRepository;

@Service
public class ArticleService {

        private final ArticleRepository articleRepository;
        private final SubscriptionRepository subscriptionRepository;
        private final CommentService commentService;
        private final ThemeRepository themeRepository;
        private final UserRepository userRepository;

        public ArticleService(ArticleRepository articleRepository, SubscriptionRepository subscriptionRepository,
                        CommentService commentService, ThemeRepository themeRepository,
                        UserRepository userRespository) {
                this.articleRepository = articleRepository;
                this.subscriptionRepository = subscriptionRepository;
                this.commentService = commentService;
                this.themeRepository = themeRepository;
                this.userRepository = userRespository;
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

        public ArticleDto createArticle(CreateArticleRequest request) {
                User author = userRepository.findById(1L)
                                .orElseThrow(() -> new RuntimeException("Utilisateur courant introuvable"));

                Theme theme = themeRepository.findById(request.themeId()).orElseThrow(
                                () -> new ThemeNotFoundException("Thème introuvable (id: " + request.themeId() + ")"));

                Article article = new Article();
                article.setTitle(request.title());
                article.setContent(request.content());
                article.setAuthor(author);
                article.setTheme(theme);

                Article saved = articleRepository.save(article);

                return convertToDto(saved);
        }
}