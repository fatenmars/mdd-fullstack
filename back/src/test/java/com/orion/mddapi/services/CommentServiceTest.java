package com.orion.mddapi.services;

import com.orion.mddapi.dto.CommentDto;
import com.orion.mddapi.dto.CreateCommentRequest;
import com.orion.mddapi.entities.Article;
import com.orion.mddapi.entities.Comment;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.ArticleNotFoundException;
import com.orion.mddapi.repositories.ArticleRepository;
import com.orion.mddapi.repositories.CommentRepository;
import com.orion.mddapi.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("Ajouter un commentaire renvoie le commentaire créé")
    void addComment_returnsCreatedComment() {
        // Arrange
        User alice = new User();
        alice.setId(1L);
        alice.setUsername("alice");

        Article article = new Article();
        article.setId(1L);

        Comment saved = new Comment();
        saved.setId(5L);
        saved.setContent("Super article");
        saved.setAuthor(alice);
        saved.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        CreateCommentRequest request = new CreateCommentRequest("Super article");

        // Act
        CommentDto result = commentService.addComment(1L, request);

        // Assert
        assertThat(result.content()).isEqualTo("Super article");
        assertThat(result.author().username()).isEqualTo("alice");
    }

    @Test
    @DisplayName("Ajouter un commentaire sur un article inexistant lève ArticleNotFoundException")
    void addComment_articleNotFound_throwsException() {
        // Arrange
        User alice = new User();
        alice.setId(1L);
        alice.setUsername("alice");

        when(userRepository.findById(1L)).thenReturn(Optional.of(alice));
        when(articleRepository.findById(999L)).thenReturn(Optional.empty());

        CreateCommentRequest request = new CreateCommentRequest("Contenu");

        // Act + Assert
        assertThatThrownBy(() -> commentService.addComment(999L, request))
                .isInstanceOf(ArticleNotFoundException.class);
    }
}