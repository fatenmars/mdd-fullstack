package com.orion.mddapi.services;

import org.springframework.stereotype.Service;
import com.orion.mddapi.repositories.ArticleRepository;
import com.orion.mddapi.repositories.CommentRepository;
import com.orion.mddapi.security.AuthenticatedUserProvider;
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
    private final ArticleRepository articleRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    public CommentService(CommentRepository commentRepository,
            ArticleRepository articleRepository, AuthenticatedUserProvider authenticatedUserProvider) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.authenticatedUserProvider = authenticatedUserProvider;
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
        User author = this.authenticatedUserProvider.getCurrentUser();

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
