package com.orion.mddapi.services;

import org.springframework.stereotype.Service;
import com.orion.mddapi.repositories.ArticleRepository;
import com.orion.mddapi.repositories.CommentRepository;
import com.orion.mddapi.repositories.UserRepository;
import com.orion.mddapi.entities.Comment;
import com.orion.mddapi.entities.User;
import com.orion.mddapi.exceptions.ArticleNotFoundException;
import com.orion.mddapi.entities.Article;
import java.util.List;
import com.orion.mddapi.dto.CommentDto;
import com.orion.mddapi.dto.CreateCommentRequest;
import com.orion.mddapi.dto.AuthorDto;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository,
            ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public List<CommentDto> getCommentsByArticleId(Long articleId) {
        return commentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public CommentDto convertToDto(Comment comment) {
        AuthorDto authorDto = new AuthorDto(comment.getAuthor().getId(), comment.getAuthor().getUsername());
        return new CommentDto(comment.getId(), comment.getContent(), authorDto, comment.getCreatedAt());
    }

    public CommentDto addComment(Long articleId, CreateCommentRequest request) {
        User author = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Utilisateur courant introuvable"));
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("Article introuvable (id: " + articleId + ")"));
        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setAuthor(author);
        comment.setArticle(article);
        Comment saved = commentRepository.save(comment);
        return convertToDto((saved));
    }
}
